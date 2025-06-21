package com.lookgen.styler.domain

import jakarta.persistence.*

@Entity
@Table(name = "photo")
class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "user_id")
    var userId: Long = 0,

    var url: String = "",

    var ord: Short = 0
)
