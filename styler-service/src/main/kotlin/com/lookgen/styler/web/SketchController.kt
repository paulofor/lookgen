package com.lookgen.styler.web

import com.lookgen.styler.repo.SketchRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sketch")
class SketchController(private val sketchRepo: SketchRepository) {

    data class SketchDto(val content: String, val tokens: Int)

    @GetMapping("/{uid}")
    fun getSketch(@PathVariable uid: Long): ResponseEntity<SketchDto> {
        val sketch = sketchRepo.findByUserId(uid) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SketchDto(sketch.content, sketch.tokens))
    }
}
