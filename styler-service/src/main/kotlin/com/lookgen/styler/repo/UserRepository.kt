package com.lookgen.styler.repo

import com.lookgen.styler.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    @Query(
        value = "SELECT * FROM \"user\" WHERE stage='PENDING_SKETCH' ORDER BY updated_at FOR UPDATE SKIP LOCKED LIMIT :limit",
        nativeQuery = true
    )
    fun findPending(@Param("limit") limit: Int): List<User>
}
