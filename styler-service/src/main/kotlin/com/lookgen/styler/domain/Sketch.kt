package com.lookgen.styler.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sketch")
class Sketch(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "user_id", unique = true)
    var userId: Long = 0,

    var content: String = "",

    var tokens: Int = 0,

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()
)
