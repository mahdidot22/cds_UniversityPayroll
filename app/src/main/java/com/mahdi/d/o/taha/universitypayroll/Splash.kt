package com.mahdi.d.o.taha.universitypayroll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val isReady = MutableStateFlow(true)

        if (isReady.value){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}