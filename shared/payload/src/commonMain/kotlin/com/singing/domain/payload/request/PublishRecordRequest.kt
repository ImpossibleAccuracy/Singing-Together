package com.singing.domain.payload.request

class PublishRecordRequest(
    var recordId: Int,
    var description: String,
    var tags: List<String>,
)
