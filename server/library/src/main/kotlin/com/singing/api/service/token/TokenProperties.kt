package com.singing.api.service.token

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.Length
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.token")
class TokenProperties(
    @Min(1)
    @Max(30)
    var expirationTime: Int,

    @Length(min = 44)
    var secret: String
)
