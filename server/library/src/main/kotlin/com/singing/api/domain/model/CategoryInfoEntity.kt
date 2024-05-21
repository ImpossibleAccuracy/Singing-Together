package com.singing.api.domain.model

import jakarta.persistence.*


@Entity
@Table(name = "CategoryInfo")
class CategoryInfoEntity(
    @Id
    @Column(name = "title", nullable = false, updatable = false)
    var title: String? = null,

    @Column(name = "publications", nullable = false, updatable = false)
    val publications: Long? = null,
) {
    @PreUpdate
    private fun preUpdate() {
        throw UnsupportedOperationException()
    }
}
