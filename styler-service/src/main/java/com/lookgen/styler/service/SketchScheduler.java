package com.lookgen.styler.service;

import com.lookgen.styler.domain.Photo;
import com.lookgen.styler.domain.Sketch;
import com.lookgen.styler.domain.Stage;
import com.lookgen.styler.domain.User;
import com.lookgen.styler.repo.PhotoRepository;
import com.lookgen.styler.repo.SketchRepository;
import com.lookgen.styler.repo.UserRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class SketchScheduler {

    private static final Logger log = LoggerFactory.getLogger(SketchScheduler.class);

    private final UserRepository userRepo;
    private final PhotoRepository photoRepo;
    private final SketchRepository sketchRepo;
    private final OpenAiClient openAi;
    private final MeterRegistry meterRegistry;

    public SketchScheduler(UserRepository userRepo, PhotoRepository photoRepo,
                           SketchRepository sketchRepo, OpenAiClient openAi,
                           MeterRegistry meterRegistry) {
        this.userRepo = userRepo;
        this.photoRepo = photoRepo;
        this.sketchRepo = sketchRepo;
        this.openAi = openAi;
        this.meterRegistry = meterRegistry;
    }

    @Scheduled(fixedDelayString = "${styler.poll-ms}")
    @Transactional
    public void tick() {
        List<User> users = userRepo.findPending(5);
        users.forEach(this::processUser);
    }

    private void processUser(User user) {
        Timer.Sample timer = Timer.start(meterRegistry);
        try {
            List<Photo> photos = photoRepo.findByUserIdOrderByOrd(user.getId());
            if (photos.size() != 5) {
                throw new IllegalStateException("Expected 5 photos");
            }
            List<String> urls = photos.stream()
                    .sorted(Comparator.comparing(Photo::getOrd))
                    .map(Photo::getUrl)
                    .toList();
            OpenAiClient.Response result = openAi.createSketch(urls, user.getStyle());
            Sketch sketch = new Sketch();
            sketch.setUserId(user.getId());
            sketch.setContent(result.getContent());
            sketch.setTokens(result.getTokens());
            sketchRepo.save(sketch);
            user.setStage(Stage.SKETCH_READY);
            user.setRetryCount((short) 0);
            meterRegistry.counter("sketch_jobs_total", "state", "done").increment();
        } catch (Exception ex) {
            log.warn("Failed to process user {}", user.getId(), ex);
            user.setRetryCount((short) (user.getRetryCount() + 1));
            if (user.getRetryCount() >= 3) {
                user.setStage(Stage.SKETCH_ERROR);
            }
            meterRegistry.counter("sketch_jobs_total", "state", "error").increment();
        } finally {
            user.setUpdatedAt(LocalDateTime.now());
            userRepo.save(user);
            timer.stop(meterRegistry.timer("sketch_latency_seconds"));
        }
    }
}
