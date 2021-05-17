package io.nats.client

import java.time.Duration
import java.util.*

fun main(args: Array<String>) {
    try {
        Nats.connect("nats://demo.nats.io").use { nc ->
            print("About to publish...")

            val payload = "payload @ ${Date()}".toByteArray()
            nc.publish("kotlin-nats-demo", payload)
            nc.flush(Duration.ofSeconds(5))

            println("Done.")
        }
    } catch (exp: Exception) {
        exp.printStackTrace()
    }
}
