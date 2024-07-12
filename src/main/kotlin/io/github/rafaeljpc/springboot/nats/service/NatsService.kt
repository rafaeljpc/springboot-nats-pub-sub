package io.github.rafaeljpc.springboot.nats.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.nats.client.Connection
import io.nats.client.Message
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.nio.charset.Charset

private val log = KotlinLogging.logger { }

@Service
class NatsService @Autowired constructor(
    private val natsConnection: Connection
) {

    @PostConstruct
    fun beanInitialized() {
        log.info { "Listening '>' - all topics" }
        natsConnection.createDispatcher(this::processMessage).subscribe(">")
    }

    fun processMessage(message: Message) {
        log.info { "message.subject = ${message.subject} - message.content=${message.data.toString(Charset.defaultCharset())}" }
    }
}