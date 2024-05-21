package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseTitleEntity
import jakarta.persistence.*

@Entity
@Table(name = "document_type")
class DocumentTypeEntity(
    id: Int? = null,
    title: String? = null,

    @Column(name = "mime_type", nullable = false)
    var mimeType: String? = null,
) : BaseTitleEntity(id, title) {
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, targetEntity = DocumentEntity::class)
    var typeDocuments: Set<DocumentEntity> = setOf()
}
