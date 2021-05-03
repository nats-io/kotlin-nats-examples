package io.nats.client

import java.time.Duration

fun main(args: Array<String>) {
    try {
        Nats.connect("nats://demo.nats.io:4222").use { nc ->
            print("Waiting for a message...")

            val sub = nc.subscribe("kotlin-nats-demo")
            nc.flush(Duration.ofSeconds(5))

            val msg = sub.nextMessage(Duration.ofHours(1))
            println("Received ${msg.subject} ${String(msg.data)}")
        }
    } catch (exp: Exception) {
        exp.printStackTrace()
    }
}
