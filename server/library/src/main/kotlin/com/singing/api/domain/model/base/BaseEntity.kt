package com.singing.api.domain.model.base

import jakarta.persistence.*

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
)
