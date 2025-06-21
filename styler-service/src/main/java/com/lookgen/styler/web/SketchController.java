package com.lookgen.styler.web;

import com.lookgen.styler.domain.Sketch;
import com.lookgen.styler.repo.SketchRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sketch")
public class SketchController {

    private final SketchRepository sketchRepo;

    public SketchController(SketchRepository sketchRepo) {
        this.sketchRepo = sketchRepo;
    }

    public static class SketchDto {
        private final String content;
        private final int tokens;

        public SketchDto(String content, int tokens) {
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

    @GetMapping("/{uid}")
    public ResponseEntity<SketchDto> getSketch(@PathVariable("uid") Long uid) {
        Sketch sketch = sketchRepo.findByUserId(uid);
        if (sketch == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new SketchDto(sketch.getContent(), sketch.getTokens()));
    }
}
