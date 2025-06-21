package com.lookgen.styler.service

import com.lookgen.styler.config.StylerProperties
import com.lookgen.styler.domain.Stage
import com.lookgen.styler.domain.Sketch
import com.lookgen.styler.domain.User
import com.lookgen.styler.repo.PhotoRepository
import com.lookgen.styler.repo.SketchRepository
import com.lookgen.styler.repo.UserRepository
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SketchScheduler(
    private val userRepo: UserRepository,
    private val photoRepo: PhotoRepository,
    private val sketchRepo: SketchRepository,
    private val openAi: OpenAiClient,
    private val meterRegistry: MeterRegistry
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelayString = "\${styler.poll-ms}")
    @Transactional
    fun tick() {
        val users = userRepo.findPending(5)
        users.forEach { processUser(it) }
    }

    private fun processUser(user: User) {
        val timer = Timer.start(meterRegistry)
        try {
            val photos = photoRepo.findByUserIdOrderByOrd(user.id)
            if (photos.size != 5) throw IllegalStateException("Expected 5 photos")
            val urls = photos.sortedBy { it.ord }.map { it.url }
            val result = openAi.createSketch(urls, user.style)
            sketchRepo.save(Sketch(userId = user.id, content = result.content, tokens = result.tokens))
            user.stage = Stage.SKETCH_READY
            user.retryCount = 0
            meterRegistry.counter("sketch_jobs_total", "state", "done").increment()
        } catch (ex: Exception) {
            log.warn("Failed to process user ${'$'}{user.id}", ex)
            user.retryCount = (user.retryCount + 1).toShort()
            if (user.retryCount >= 3) user.stage = Stage.SKETCH_ERROR
            meterRegistry.counter("sketch_jobs_total", "state", "error").increment()
        } finally {
            user.updatedAt = LocalDateTime.now()
            userRepo.save(user)
            timer.stop(meterRegistry.timer("sketch_latency_seconds"))
        }
    }
}
