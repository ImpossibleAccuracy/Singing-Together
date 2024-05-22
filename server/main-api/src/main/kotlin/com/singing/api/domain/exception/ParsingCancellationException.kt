package com.singing.api.domain.exception

class ParsingCancellationException : RuntimeException {
    constructor() : super()
    constructor(cause: Throwable) : super(cause)
}
