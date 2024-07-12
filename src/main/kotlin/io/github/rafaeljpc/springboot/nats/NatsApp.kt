package io.github.rafaeljpc.springboot.nats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NatsApp

fun main(args: Array<String>) {
    runApplication<NatsApp>(*args)
}