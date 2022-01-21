package com.mahdi.d.o.taha.universitypayroll.notification_Managment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityAddNotificationBinding

class Add_Notification : AppCompatActivity() {
    private var _binding: ActivityAddNotificationBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSend.setOnClickListener {

            }
        }

    }
}