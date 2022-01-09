package com.mahdi.d.o.taha.universitypayroll.admin.update

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.SpinnerAdapter
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityUpdateBinding

class Update_Emp : AppCompatActivity() {
    private var _binding: ActivityUpdateBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        val username = intent.getStringExtra("username")
        super.onCreate(savedInstanceState)
        _binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val cotract_type_items = resources.getStringArray(R.array.contract_type_array)
        val paymentMethods = resources.getStringArray(R.array.payment_method_array)
        binding.edNewUsername.setText(username)
        inflateSpinner(cotract_type_items, binding.spNewContractType)
        inflateSpinner(paymentMethods, binding.spPaymentType)
    }

    private fun inflateSpinner(array: Array<String>, spinner: Spinner) {
        val spinnerAdapter = SpinnerAdapter(this, array)
        spinner.adapter = spinnerAdapter.spinnerAdapter
        spinnerAdapter.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        onItemClickListener(spinner, array)
    }

    private fun onItemClickListener(spinner: Spinner, items: Array<String>) {
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val value = parent!!.getItemAtPosition(position).toString()
                    if (value == items[0]) {
                        (view as TextView).setTextColor(Color.GRAY)
                    }
                    if (spinner == binding.spNewContractType) {
                        val fixedPrice = resources.getStringArray(R.array.fixed_price_array)
                        val hourlyRate = resources.getStringArray(R.array.hourly_rate_array)
                        val acceptedHolidays = resources.getStringArray(R.array.holidays_array)
                        if (value == items[1]) {
                            inflateSpinner(fixedPrice, binding.spPrice)
                            inflateSpinner(acceptedHolidays, binding.spAcceptedHolidays)
                        } else if (value == items[2]) {
                            inflateSpinner(hourlyRate, binding.spPrice)
                            binding.spAcceptedHolidays.isEnabled = false
                        }
                    }
                }

            }
    }
}