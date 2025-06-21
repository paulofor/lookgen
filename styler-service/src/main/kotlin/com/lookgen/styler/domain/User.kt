package com.lookgen.styler.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    var stage: Stage = Stage.CAPTURED,

    @Column(name = "retry_count")
    var retryCount: Short = 0,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    var style: String? = null
)
