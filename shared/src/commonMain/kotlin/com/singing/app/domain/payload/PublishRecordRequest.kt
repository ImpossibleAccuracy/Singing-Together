package com.singing.app.domain.payload

class PublishRecordRequest(
    var recordId: Int,
    var description: String,
    var tags: List<String>,
)
