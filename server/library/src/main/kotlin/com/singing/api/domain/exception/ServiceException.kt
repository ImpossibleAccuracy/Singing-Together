package com.singing.api.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
abstract class ServiceException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}
