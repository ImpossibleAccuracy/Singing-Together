package com.singing.api.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException : ServiceException {
    constructor() : super()
    constructor(message: String?) : super(message)
}
