package com.freewind.seastarvideodemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.freewind.seastarvideo.SeaStarClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed(
            {
                SeaStarClient.instance.SSC_StartHomeActivity(this@MainActivity)
                finish()
            }, 3000)
    }
}