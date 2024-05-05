package com.singing.api.domain.model.base

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass


@MappedSuperclass
abstract class BaseTitleEntity(
    id: Int? = null,

    @Column(name = "title", nullable = false)
    var title: String? = null,
) : BaseEntity(id)
