package com.lookgen.styler.service

import com.lookgen.styler.config.OpenAiProperties
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class OpenAiClient(
    private val props: OpenAiProperties,
    private val meterRegistry: MeterRegistry
) {
    private val client = WebClient.builder()
        .baseUrl("https://api.openai.com/v1/chat/completions")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer ${'$'}{props.apiKey}")
        .build()

    data class Response(val content: String, val tokens: Int)

    fun createSketch(urls: List<String>, style: String?): Response {
        val payload = mapOf(
            "model" to "gpt-4o-mini",
            "stream" to false,
            "messages" to listOf(
                mapOf("role" to "system", "content" to "Você é um estilista"),
                mapOf(
                    "role" to "user",
                    "content" to buildContent(urls, style)
                )
            )
        )

        return try {
            val result = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map::class.java)
                .doOnError { meterRegistry.counter("openai_requests_total", "status", "error").increment() }
                .block(props.readTimeout)

            meterRegistry.counter("openai_requests_total", "status", "success").increment()

            val choice = ((result?.get("choices") as List<*>?)?.firstOrNull() as Map<*, *>?)
            val message = choice?.get("message") as Map<*, *>?
            val content = message?.get("content") as String? ?: ""
            val usage = result?.get("usage") as Map<*, *>?
            val tokens = (usage?.get("total_tokens") as Number?)?.toInt() ?: 0
            usage?.forEach { (k, v) ->
                if (v is Number) {
                    meterRegistry.counter("openai_token_usage_total", "type", k.toString()).increment(v.toDouble())
                }
            }
            Response(content, tokens)
        } catch (ex: Exception) {
            meterRegistry.counter("openai_requests_total", "status", "error").increment()
            throw ex
        }
    }

    private fun buildContent(urls: List<String>, style: String?): List<Any> {
        val list = mutableListOf<Any>(mapOf("type" to "text", "text" to "Crie um esboço:"))
        urls.forEach { u ->
            list += mapOf("type" to "image_url", "image_url" to mapOf("url" to u))
        }
        style?.let { list += mapOf("type" to "text", "text" to it) }
        list += mapOf("type" to "text", "text" to "Bullets, paleta e peças-chave.")
        return list
    }
}
