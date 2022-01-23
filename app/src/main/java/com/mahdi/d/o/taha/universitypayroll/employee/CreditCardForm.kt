package com.mahdi.d.o.taha.universitypayroll.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityCreditCardFormBinding

class CreditCardForm : AppCompatActivity() {
    private var _binding:ActivityCreditCardFormBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreditCardFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnTransfer.setOnClickListener {
            Toast.makeText(this, "Transferred!!", Toast.LENGTH_SHORT).show()
        }
    }
}