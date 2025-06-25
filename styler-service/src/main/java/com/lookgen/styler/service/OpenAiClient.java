package com.lookgen.styler.service;

import com.lookgen.styler.config.OpenAiProperties;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

@Component
public class OpenAiClient {

    private final OpenAiProperties props;
    private final MeterRegistry meterRegistry;
    private final WebClient client;
    private static final Logger log = LoggerFactory.getLogger(OpenAiClient.class);

    public static class Response {
        private final String content;
        private final int tokens;

        public Response(String content, int tokens) {
            this.content = content;
            this.tokens = tokens;
        }

        public String getContent() {
            return content;
        }

        public int getTokens() {
            return tokens;
        }
    }

    public OpenAiClient(OpenAiProperties props, MeterRegistry meterRegistry) {
        this.props = props;
        this.meterRegistry = meterRegistry;
        this.client = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getApiKey())
                .build();
    }

    public Response createSketch(List<String> urls, String style) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "gpt-4o-mini");
        payload.put("stream", false);
        payload.put("messages", List.of(
                Map.of("role", "system", "content", "Você é um estilista"),
                Map.of("role", "user", "content", buildContent(urls, style))
        ));

        try {
            if (log.isDebugEnabled()) {
                log.debug("Enviando requisição ao OpenAI: {}", payload);
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> result = client.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .doOnError(e -> meterRegistry.counter("openai_requests_total", "status", "error").increment())
                    .block(props.getReadTimeout());

            meterRegistry.counter("openai_requests_total", "status", "success").increment();

            Map<?, ?> choice = null;
            if (result != null) {
                Object choicesObj = result.get("choices");
                if (choicesObj instanceof List<?> list && !list.isEmpty()) {
                    Object first = list.get(0);
                    if (first instanceof Map<?, ?> map) {
                        choice = map;
                    }
                }
            }
            Map<?, ?> message = choice == null ? null : (Map<?, ?>) choice.get("message");
            String content = message == null ? "" : Objects.toString(message.get("content"), "");
            Map<?, ?> usage = result == null ? null : (Map<?, ?>) result.get("usage");
            int tokens = 0;
            if (usage != null) {
                Object total = usage.get("total_tokens");
                if (total instanceof Number n) {
                    tokens = n.intValue();
                }
                for (Map.Entry<?, ?> entry : usage.entrySet()) {
                    Object v = entry.getValue();
                    if (v instanceof Number n) {
                        meterRegistry.counter("openai_token_usage_total", "type", entry.getKey().toString())
                                .increment(n.doubleValue());
                    }
                }
            }
            return new Response(content, tokens);
        } catch (WebClientResponseException ex) {
            meterRegistry.counter("openai_requests_total", "status", "error").increment();
            log.error("Erro na chamada ao OpenAI - status: {}, body: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception ex) {
            meterRegistry.counter("openai_requests_total", "status", "error").increment();
            log.error("Falha inesperada na chamada ao OpenAI", ex);
            throw ex;
        }
    }

    private List<Object> buildContent(List<String> urls, String style) {
        List<Object> list = new ArrayList<>();
        list.add(Map.of("type", "text", "text", "Crie um esboço:"));
        for (String u : urls) {
            list.add(Map.of(
                    "type", "image_url",
                    "image_url", Map.of(
                            "url", u,
                            "detail", "low"
                    )));
        }
        if (style != null) {
            list.add(Map.of("type", "text", "text", style));
        }
        list.add(Map.of("type", "text", "text", "Bullets, paleta e peças-chave."));
        return list;
    }
}
