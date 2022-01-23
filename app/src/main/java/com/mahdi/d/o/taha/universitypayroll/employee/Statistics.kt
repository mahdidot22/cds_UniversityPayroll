package com.mahdi.d.o.taha.universitypayroll.employee

import android.Manifest.permission.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityStatisticsBinding
import androidx.core.app.ActivityCompat
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import java.io.File
import java.util.*
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import com.mahdi.d.o.taha.universitypayroll.notification_Managment.Notifications_Managment
import kotlin.collections.ArrayList


class Statistics : AppCompatActivity() {
    private var _binding: ActivityStatisticsBinding? = null
    private val binding get() = _binding!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    lateinit var partList: ArrayList<Emp.Casual>
    lateinit var fullList: ArrayList<Emp.Full>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStatisticsBinding.inflate(layoutInflater)
        _constants = Constants()
        partList = arrayListOf()
        fullList = arrayListOf()
        val type = intent.getStringExtra("type")
        infalteViews(type.toString())
        setContentView(binding.root)
        binding.apply {
            notifications.setOnClickListener {
                startActivity(
                    Intent(
                        this@Statistics,
                        Notifications_Managment::class.java
                    )
                )
            }
            btnPaymentForm.setOnClickListener {
                if (type == "Part-Time"){
                    startActivity(Intent(this@Statistics, PaymentForm::class.java).putExtra("user",partList).putExtra("type",type.toString()))
                }else{
                    startActivity(Intent(this@Statistics, PaymentForm::class.java).putExtra("user",fullList).putExtra("type",type.toString()))
                }
            }
        }
    }

    private fun infalteViews(type: String) {
        if (type == "Part-Time") {
            val list =
                intent.getParcelableArrayListExtra<Emp.Casual>("user")!! as ArrayList<Emp.Casual>
            list.forEach {
                binding.apply {
                    empName.text = it.username
                    empId.text = it.id
                    empContractType.text = it.contract
                    holidaysLayout.visibility = View.GONE
                    workSince.text = it.year
                    when (it.sal) {
                        "5" -> {
                            currentPayment.text = "${it.sal.toInt()*4*20}"
                                totalPayments.text = currentPayment.text.toString()
                        }
                        "10" -> {
                            currentPayment.text = "${it.sal.toInt()*4*15}"
                            totalPayments.text = currentPayment.text.toString()
                        }
                        "15" -> {
                            currentPayment.text = "${it.sal.toInt()*4*10}"
                            totalPayments.text = currentPayment.text.toString()
                        }
                    }
                }
            }
            partList = list
        } else {
            val list =
                intent.getParcelableArrayListExtra<Emp.Casual>("user")!! as ArrayList<Emp.Full>
            list.forEach {
                binding.apply {
                    empName.text = it.username
                    empId.text = it.id
                    empContractType.text = it.contract
                    holidaysLayout.visibility = View.VISIBLE
                    acceptedHolidays.text = it.holidays
                    validHolidays.text = it.remainingHolidays
                    workSince.text = it.year
                    currentPayment.text = it.sal
                    totalPayments.text = it.sal
                }
            }
            fullList = list
        }
    }
}