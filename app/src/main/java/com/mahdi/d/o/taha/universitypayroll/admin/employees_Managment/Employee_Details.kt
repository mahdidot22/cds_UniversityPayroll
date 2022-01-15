package com.mahdi.d.o.taha.universitypayroll.admin.employees_Managment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityDetailsBinding
import com.mahdi.d.o.taha.universitypayroll.model.Emp

class Employee_Details : AppCompatActivity() {
    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val emp = intent.getParcelableExtra<Emp>("emp")
        val empInfo = intent.getParcelableExtra<Emp.EpmJopInfo>("emp")
        binding.apply {
            username.text = emp!!.emp_name
            empId.text = emp.emp_id
            empContractType.text = emp.emp_contract_type
            acceptedHolidays.text = empInfo!!.emp_accepted_holidays
            validHolidays.text = empInfo.emp_remaining_holidays
            paymentDay.text = empInfo.emp_payment_date
            paymentType.text = empInfo.emp_payment_type
            totalDays.text = empInfo.emp_total_work_days
            totalPayments.text = empInfo.emp_total_salaries
            btnPay.setOnClickListener {
            }
        }
    }
}