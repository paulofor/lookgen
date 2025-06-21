package com.lookgen.styler.repo

import com.lookgen.styler.domain.Photo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PhotoRepository : JpaRepository<Photo, Long> {
    fun findByUserIdOrderByOrd(userId: Long): List<Photo>
}
