package com.singing.api

import javafx.application.Application
import javafx.stage.Stage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.singing"])
@ConfigurationPropertiesScan("com.singing")
class SpringApplication

class JavaFXApplication : Application() {
    override fun init() {
        val args = parameters.raw.toTypedArray<String>()

        runApplication<SpringApplication>(*args)
    }

    override fun start(p0: Stage?) {}
}

fun main(args: Array<String>) {
    Application.launch(JavaFXApplication::class.java, *args)
}
