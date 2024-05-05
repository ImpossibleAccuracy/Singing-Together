package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseTitleEntity
import jakarta.persistence.*

@Entity
@Table(name = "document_type")
class DocumentType(
    id: Int? = null,

    @Column(name = "mime_type", nullable = false)
    var mimeType: String? = null,
) : BaseTitleEntity(id) {
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, targetEntity = Document::class)
    var typeDocuments: Set<Document> = setOf()
}
