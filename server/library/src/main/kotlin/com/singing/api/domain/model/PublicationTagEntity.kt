package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseTitleEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "publication_tag")
class PublicationTagEntity(
    id: Int? = null,
    title: String? = null,
) : BaseTitleEntity(id, title) {
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY, targetEntity = PublicationEntity::class)
    var publications: Set<PublicationEntity> = setOf()
}
