package example.nats


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import io.nats.client.Connection


open interface IDataCollector {
    fun setConnect(connect: Boolean)
    fun setResponse(response: String)
}

class MainActivity : AppCompatActivity(), IDataCollector {
    lateinit var nats: NatsManager;
    lateinit var text: TextView
    var responses: String = ""
    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPermissions()
        //this.getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        val hide = supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.response)
        text.text = responses
        nats = NatsManager(this)
        var btn : MaterialButton = findViewById(R.id.connect)
        btn.setOnClickListener { nats.connect() }

        var btn2 : MaterialButton = findViewById(R.id.pub)
        btn2.setOnClickListener { nats.pub("test", "THIS IS A TEST MSG") }
    }


    private fun setupPermissions() {
        val TAG = "Permissions Manager"
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")

                Log.i(TAG, "ENTRO")
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access the microphone is required for this app to record audio.") .setTitle("Permission required")
                builder.setPositiveButton("OK") { dialog, id ->
                    Log.i(TAG, "Clicked")
                    makeRequest()
                }
                val dialog = builder.create()
                dialog.show()

        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val TAG = "Permissions Manager"
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }



    override fun setConnect(connect: Boolean) {
        val natsGreenBar: LinearProgressIndicator = findViewById(R.id.connectIndicator)
        if(connect == true){
            natsGreenBar.setIndicatorColor(Color.parseColor("#23B221"))
            natsGreenBar.visibility= View.VISIBLE
        }
    }

    override fun setResponse(response: String) {
        responses = responses+response+"\n"
        println(responses)
        text.text= responses
    }


}

