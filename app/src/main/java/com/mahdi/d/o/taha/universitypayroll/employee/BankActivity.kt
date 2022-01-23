package com.mahdi.d.o.taha.universitypayroll.employee

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahdi.d.o.taha.universitypayroll.databinding.BankItemBinding
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import java.text.SimpleDateFormat
import java.util.*

class BankActivity : AppCompatActivity() {
    private var _binding:BankItemBinding? = null
    private val binding get() = _binding!!
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = BankItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val type = intent.getStringExtra("type")
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        if (type == "Part-Time") {
            val list = intent.getParcelableArrayListExtra<Emp.Casual>("user")!!
            list.forEach {
                binding.apply {
                    when (it.sal) {
                        "5" -> {
                            tvEmpName.text = it.username
                            sal.text = "${it.sal.toInt() * 4 * 20}"
                            date.text = currentDateAndTime

                        }
                        "10" -> {
                            tvEmpName.text = it.username
                            sal.text = "${it.sal.toInt() * 4 * 15}"
                            date.text = currentDateAndTime

                        }
                        "15" -> {
                            tvEmpName.text = it.username
                            sal.text = "${it.sal.toInt() * 4 * 10}"
                            date.text = currentDateAndTime
                        }
                    }
                }
            }
        } else {
            val list = intent.getParcelableArrayListExtra<Emp.Full>("user")!!
            list.forEach {
                binding.apply {
                    tvEmpName.text = it.username
                    sal.text = it.sal
                    date.text = currentDateAndTime
                }
            }
        }
    }
}