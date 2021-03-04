package edu.mtu.sercsoundsampler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window

class MainActivity : AppCompatActivity() {
    public val TIME_OUT_MS = 1000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val handler = Handler()
        handler.postDelayed(Runnable {
//            val intent = Intent(this, SampleActivity::class.java)
            val intent = Intent(this, SamplingAllInOneActivity::class.java)
            startActivity(intent)
            finish()
        }, TIME_OUT_MS)
    }
}