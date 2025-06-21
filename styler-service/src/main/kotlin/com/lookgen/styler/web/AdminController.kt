package com.lookgen.styler.web

import com.lookgen.styler.domain.Stage
import com.lookgen.styler.repo.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController(private val userRepo: UserRepository) {

    @PostMapping("/v1/admin/replay")
    @Transactional
    fun replay(@RequestParam uid: Long): ResponseEntity<Void> {
        val user = userRepo.findById(uid).orElse(null) ?: return ResponseEntity.notFound().build()
        user.stage = Stage.PENDING_SKETCH
        user.retryCount = 0
        userRepo.save(user)
        return ResponseEntity.accepted().build()
    }
}
