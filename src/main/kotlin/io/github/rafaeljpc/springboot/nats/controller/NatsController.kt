package io.github.rafaeljpc.springboot.nats.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.rafaeljpc.springboot.nats.dto.NatsDTO
import io.nats.client.Connection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger {}

@RestController("nats-controller")
@RequestMapping(path = ["nats"])
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class NatsController @Autowired constructor(
    private val natsConnection: Connection
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun post(@RequestBody message: NatsDTO) {
        log.info { "Posting message subject=${message.subject}, message=${message.message}" }
        natsConnection.publish(message.subject, message.message.toByteArray())
    }
}