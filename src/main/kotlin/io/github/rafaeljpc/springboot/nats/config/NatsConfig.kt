package io.github.rafaeljpc.springboot.nats.config

import io.nats.client.Nats
import io.nats.client.Options
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NatsConfig(
    @Value("\${app.nats-server}") private val natsServerUrl: String
) {

    @Bean
    fun natsConnection() = run {
        val opt = Options.Builder().server(natsServerUrl).build()
        Nats.connect(opt)
    }
}