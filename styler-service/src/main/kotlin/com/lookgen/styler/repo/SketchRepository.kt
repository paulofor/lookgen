package com.lookgen.styler.repo

import com.lookgen.styler.domain.Sketch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SketchRepository : JpaRepository<Sketch, Long> {
    fun findByUserId(userId: Long): Sketch?
}
