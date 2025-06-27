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
import com.lookgen.styler.service.GcpStorageService;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class OpenAiClient {

    private final OpenAiProperties props;
    private final MeterRegistry meterRegistry;
    private final WebClient chatClient;
    private final WebClient imageClient;
    private final WebClient downloadClient;
    private final GcpStorageService storage;
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

    public OpenAiClient(OpenAiProperties props, MeterRegistry meterRegistry, GcpStorageService storage) {
        this.props = props;
        this.meterRegistry = meterRegistry;
        this.storage = storage;
        this.chatClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getApiKey())
                .build();
        this.imageClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/images/generations")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getApiKey())
                .build();
        this.downloadClient = WebClient.builder().build();
    }

    public Response createSketch(List<String> urls, String style) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "gpt-4o-mini");
        payload.put("stream", false);
        payload.put("messages", List.of(
                Map.of("role", "system", "content", "Você é um estilista"),
                Map.of("role", "user", "content", buildContent(urls, style))
        ));

        log.info("Prompt (sketch) executado: {}", payload);

        try {
            if (log.isDebugEnabled()) {
                log.debug("Enviando requisição ao OpenAI: {}", payload);
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> result = chatClient.post()
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

    public Response createLookImage(List<String> urls, String style) {
        Map<String, Object> chatPayload = new HashMap<>();
        chatPayload.put("model", "gpt-4o-mini");
        chatPayload.put("stream", false);
        chatPayload.put("messages", List.of(
                Map.of("role", "system", "content", "Você é um estilista"),
                Map.of("role", "user", "content", buildPromptContent(urls, style))
        ));

        try {
            if (log.isDebugEnabled()) {
                log.debug("Enviando requisição ao OpenAI (chat): {}", chatPayload);
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> chatResult = chatClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(chatPayload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .doOnError(e -> meterRegistry.counter("openai_requests_total", "status", "error").increment())
                    .block(props.getReadTimeout());

            meterRegistry.counter("openai_requests_total", "status", "success").increment();

            Map<?, ?> choice = null;
            if (chatResult != null) {
                Object choicesObj = chatResult.get("choices");
                if (choicesObj instanceof List<?> list && !list.isEmpty()) {
                    Object first = list.get(0);
                    if (first instanceof Map<?, ?> map) {
                        choice = map;
                    }
                }
            }
            Map<?, ?> message = choice == null ? null : (Map<?, ?>) choice.get("message");
            String prompt = message == null ? "" : Objects.toString(message.get("content"), "");

            log.info("Prompt (imagem) executado: {}", prompt);

            Map<?, ?> usage = chatResult == null ? null : (Map<?, ?>) chatResult.get("usage");
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

            Map<String, Object> imgPayload = new HashMap<>();
            imgPayload.put("model", "dall-e-3");
            imgPayload.put("prompt", prompt);
            imgPayload.put("n", 1);
            imgPayload.put("size", "1024x1024");

            if (log.isDebugEnabled()) {
                log.debug("Enviando requisição ao OpenAI (image): {}", imgPayload);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> imgResult = imageClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(imgPayload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .doOnError(e -> meterRegistry.counter("openai_requests_total", "status", "error").increment())
                    .block(props.getReadTimeout());

            String url = "";
            if (imgResult != null) {
                Object dataObj = imgResult.get("data");
                if (dataObj instanceof List<?> list && !list.isEmpty()) {
                    Object first = list.get(0);
                    if (first instanceof Map<?, ?> map) {
                        Object u = map.get("url");
                        if (u != null) {
                            url = u.toString();
                        }
                    }
                }
            }
            if (!url.isBlank()) {
                byte[] imgBytes = downloadClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block(props.getReadTimeout());
                url = storage.upload(imgBytes, "image/png");
            }

            return new Response(url, tokens);
        } catch (WebClientResponseException ex) {
            meterRegistry.counter("openai_requests_total", "status", "error").increment();
            log.error("Erro na chamada ao OpenAI", ex);
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

    private List<Object> buildPromptContent(List<String> urls, String style) {
        List<Object> list = new ArrayList<>();
        list.add(Map.of("type", "text", "text", "Gere um prompt curto para DALL-E:"));
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
        list.add(Map.of("type", "text", "text", "Descreva uma combinação das peças em estilo de desenho de estilista profissional."));
        list.add(Map.of("type", "text", "text", "Responda apenas com o prompt."));
        return list;
    }
}
