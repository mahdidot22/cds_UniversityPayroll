package com.mahdi.d.o.taha.universitypayroll.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahdi.d.o.taha.universitypayroll.databinding.BankItemBinding
import com.mahdi.d.o.taha.universitypayroll.databinding.CheckItemBinding
import com.mahdi.d.o.taha.universitypayroll.model.Emp

class CheckActivity : AppCompatActivity() {
    private var _binding: CheckItemBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = CheckItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val type = intent.getStringExtra("type")
        if (type == "Part-Time") {
            val list = intent.getParcelableArrayListExtra<Emp.Casual>("user")!!
            list.forEach {
                binding.apply {
                    when (it.sal) {
                        "5" -> {
                            tvEmpName.text = it.username
                            tvSal.text = "${it.sal.toInt() * 4 * 20}"

                        }
                        "10" -> {
                            tvEmpName.text = it.username
                            tvSal.text = "${it.sal.toInt() * 4 * 15}"

                        }
                        "15" -> {
                            tvEmpName.text = it.username
                            tvSal.text = "${it.sal.toInt() * 4 * 10}"

                        }
                    }
                }
            }
        } else {
            val list = intent.getParcelableArrayListExtra<Emp.Full>("user")!!
            list.forEach {
                binding.apply {
                    tvEmpName.text = it.username
                    tvSal.text = it.sal
                }
            }
        }
    }
}