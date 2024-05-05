package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseTitleEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "publication_tag")
class PublicationTag(
    id: Int? = null,
) : BaseTitleEntity(id) {
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY, targetEntity = Publication::class)
    var publications: Set<Publication> = setOf()
}
