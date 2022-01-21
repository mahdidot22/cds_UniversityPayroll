package com.mahdi.d.o.taha.universitypayroll.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityDetailsBinding
import com.mahdi.d.o.taha.universitypayroll.model.Emp

class Employee_Details : AppCompatActivity() {
    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val empInfo = intent.getParcelableExtra<Emp.EpmJopInfo>("emp")
        binding.apply {
            username.text = empInfo!!.emp.emp_name
            empId.text = empInfo.emp.emp_id
            empContractType.text = empInfo.emp.emp_contract_type
            if (empInfo.emp.emp_contract_type == "Part-Time") {
                price.text = "${empInfo.emp_salary} per hour"
                holidaysLayout1.visibility =View.GONE
                holidaysLayout2.visibility =View.GONE
            } else {
                holidaysLayout1.visibility = View.VISIBLE
                holidaysLayout2.visibility = View.VISIBLE
                price.text = "${empInfo.emp_salary} monthly"
            }
            acceptedHolidays.text = "Accepted holidays: ${empInfo.emp_accepted_holidays}"
            validHolidays.text = "Remaining holidays: ${empInfo.emp_remaining_holidays}"
            totalDays.text = "Works since: ${empInfo.emp_date_work_day}"
            totalPayments.text = "Total salaries: ${empInfo.emp_total_salaries}"
        }
    }
}