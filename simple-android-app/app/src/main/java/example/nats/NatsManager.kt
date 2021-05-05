package example.nats

import android.util.Log
import io.nats.client.Connection
import io.nats.client.Message
import io.nats.client.Nats
import io.nats.client.Options
import java.nio.charset.StandardCharsets


class NatsManager(datacollector: MainActivity) {

    val TAG = "Nats Service"
    lateinit var nc : Connection
    val datacollector = datacollector
    var connect = false

    fun connect() {
        Log.d(TAG, "TRY TO CONNECT")
        Thread {
            val options: Options = Options.Builder().server("nats://demo.nats.io:4222").build()

            try {
                nc = Nats.connect(options)
                Log.d(TAG, "Connected to Nats server ${options.servers.first()}")
                connect= true
                datacollector.setConnect(true)


                val d = nc.createDispatcher { msg: Message? -> }

                val s = d.subscribe("test") { msg ->
                    val response = String(msg.data, StandardCharsets.UTF_8)
                    datacollector.setResponse(response)
                    println("Message received (up to 100 times): $response")
                }


            } catch (exp: Exception) {
                println(exp.printStackTrace())
                connect = false
                datacollector.setConnect(true)
            }
        }.start()

    }

    fun pub(topic: String, msg: String){
        nc.publish(topic, msg.toByteArray(StandardCharsets.UTF_8))
        Log.d(TAG, "Published msg ${msg} on topic ${topic}")
    }

    fun close(){
        nc.close();
        Log.d(TAG, "Nats connection close")
    }

}
