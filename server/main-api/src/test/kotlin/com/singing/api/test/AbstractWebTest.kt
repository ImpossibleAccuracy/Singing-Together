package com.singing.api.test

import com.ninjasquad.springmockk.MockkBean
import com.singing.api.SpringTestApplication
import com.singing.api.domain.model.TestFile
import com.singing.api.security.SecurityConfig
import com.singing.api.security.builder.service.SecurityService
import com.singing.api.setup.MockAuth
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Import
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMultipartHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc

@ContextConfiguration(classes = [SpringTestApplication::class])
@ConfigurationPropertiesScan("com.singing")
@Import(SecurityConfig::class)
abstract class AbstractWebTest {
    @MockkBean
    private lateinit var securityService: SecurityService

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setupAuthService() {
        MockAuth.setupAuth(securityService)
    }

    fun MockMultipartHttpServletRequestDsl.multipart(name: String, testFile: TestFile) {
        val fileObj = testFile.get()

        file(
            MockMultipartFile(
                name,
                fileObj.name,
                null,
                fileObj.readBytes()
            )
        )
    }
}
