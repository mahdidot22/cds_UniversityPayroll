package com.mahdi.d.o.taha.universitypayroll.admin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.contains
import androidx.core.view.isNotEmpty
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.SpinnerAdapter
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityUpdateBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp

class Update_Emp : AppCompatActivity() {
    private var _binding: ActivityUpdateBinding? = null
    private val binding get() = _binding!!
    private var _db: FirebaseFirestore? = null
    private val db get() = _db!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    override fun onCreate(savedInstanceState: Bundle?) {
        val emp = intent.getParcelableExtra<Emp.EpmJopInfo>("emp")
        super.onCreate(savedInstanceState)
        _binding = ActivityUpdateBinding.inflate(layoutInflater)
        _db = Firebase.firestore
        _constants = Constants()
        setContentView(binding.root)
        val cotract_type_items = resources.getStringArray(R.array.contract_type_array)
        binding.apply {
            edNewUsername.setText(emp!!.emp.emp_name)
            inflateSpinner(cotract_type_items, spNewContractType, emp)
            editContract.setOnClickListener {
                validation(
                    emp.emp.emp_id.toString(),
                    edNewUsername.text.toString(),
                    edNewPsw.text.toString(),
                    spNewContractType.selectedItem.toString(),
                    spPrice.selectedItem.toString(),
                    "${spAcceptedHolidays.selectedItem}"
                )
            }
        }
    }

    private fun validation(
        id: String,
        fullName: String,
        psw: String,
        userContractType: String,
        salary: String,
        holidays: String
    ) {
        if (fullName.isNotEmpty() && userContractType.isNotEmpty() && salary.isNotEmpty() && holidays.isNotEmpty()) {
            binding.apply {
                if (spNewContractType.selectedItemPosition == 0 || spPrice.selectedItemPosition == 0 || spAcceptedHolidays.selectedItemPosition == 0) {
                    constants.toast(this@Update_Emp, "Please check your list choices!")
                } else if (spNewContractType.selectedItemPosition == 1) {
                    if (spPrice.selectedItemPosition == 0 || spAcceptedHolidays.selectedItemPosition == 0) {
                        constants.toast(this@Update_Emp, "Please check your list choices!")
                    } else {
                        editContract(id, fullName, psw, holidays, salary, userContractType)
                    }
                } else if (spNewContractType.selectedItemPosition == 2) {
                    if (spPrice.selectedItemPosition == 0) {
                        constants.toast(this@Update_Emp, "Please check your list choices!")
                    } else {
                        editContract(id, fullName, psw, salary, userContractType)
                    }
                }
            }
        } else {
            constants.fillFields(binding)
        }
    }

    private fun editContract(
        id: String,
        username: String,
        psw: String,
        acceptedHolidays: String,
        salary: String,
        contract: String
    ) {
        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng").collection("emp")
            .whereEqualTo("id", id).get().addOnSuccessListener {
                if (it.isEmpty) {
                    constants.toast(
                        this,
                        "MISSY ID! PLEASE RETRY AGAIN LATER".lowercase()
                    )
                } else {
                    val update = if (psw.isEmpty()) {
                        hashMapOf(
                            "fullName" to username,
                            "acceptedHolidays" to acceptedHolidays,
                            "salary" to salary,
                            "contract" to contract,
                            "remainingHolidays" to (acceptedHolidays.toInt()/2).toString()
                        )
                    } else {
                        hashMapOf(
                            "fullName" to username,
                            "acceptedHolidays" to acceptedHolidays,
                            "salary" to salary,
                            "contract" to contract,
                            "psw" to psw,
                            "remainingHolidays" to (acceptedHolidays.toInt()/2).toString()
                        )
                    }
                    for (doc in it) {
                        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng")
                            .collection("emp").document(doc.id).update(update as Map<String, Any>).addOnSuccessListener {
                                constants.addingMsg(binding, "user updated successfully!!")
                                onBackPressed()
                            }
                    }
                }
            }.addOnFailureListener {
                constants.toast(this, it.localizedMessage!!)
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,Employees_Mnagment::class.java))
        this.finish()
    }
    private fun editContract(
        id: String,
        username: String,
        psw: String,
        salary: String,
        contract: String
    ) {
        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng").collection("emp")
            .whereEqualTo("id", id).get().addOnSuccessListener {
                if (it.isEmpty) {
                    constants.toast(
                        this,
                        "MISSY ID! PLEASE RETRY AGAIN LATER".lowercase()
                    )
                } else {
                    val update = if (psw.isEmpty()) {
                        hashMapOf(
                            "fullName" to username,
                            "salary" to salary,
                            "contract" to contract
                        )
                    } else {
                        hashMapOf(
                            "fullName" to username,
                            "psw" to psw,
                            "salary" to salary,
                            "contract" to contract
                        )
                    }

                    for (doc in it) {
                        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng")
                            .collection("emp").document(doc.id).update(update as Map<String, Any>)
                            .addOnSuccessListener {
                                deleteFields(doc)
                                constants.addingMsg(binding, "user updated successfully!!")
                            }
                    }
                }
            }.addOnFailureListener {
                constants.toast(this, it.localizedMessage!!)
            }
    }
    private fun deleteFields(doc:QueryDocumentSnapshot){
        val updates = hashMapOf<String, Any>(
            "acceptedHolidays" to FieldValue.delete(),
            "remainingHolidays" to FieldValue.delete()
        )
        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng")
            .collection("emp").document(doc.id).update(updates)
    }
    private fun inflateSpinner(array: Array<String>, spinner: Spinner, emp: Emp.EpmJopInfo) {
        val spinnerAdapter = SpinnerAdapter(this, array)
        spinner.adapter = spinnerAdapter.spinnerAdapter
        spinnerAdapter.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        for (i in array.indices) {
            if (array[i] == emp.emp.emp_contract_type) {
                spinner.setSelection(i)
            }
        }
        onItemClickListener(spinner, array, emp)
    }

    private fun onItemClickListener(spinner: Spinner, items: Array<String>, emp: Emp.EpmJopInfo) {
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
                    if (spinner.isNotEmpty()) {
                        if (value == items[0]) {
                            (view as TextView).setTextColor(Color.GRAY)
                        }
                    }
                    if (spinner == binding.spNewContractType) {
                        val fixedPrice = resources.getStringArray(R.array.fixed_price_array)
                        val hourlyRate = resources.getStringArray(R.array.hourly_rate_array)
                        val acceptedHolidays = resources.getStringArray(R.array.holidays_array)
                        if (value == items[1]) {
                            inflateSpinner(fixedPrice, binding.spPrice, emp)
                            for (i in fixedPrice.indices) {
                                if (fixedPrice[i] == emp.emp_salary) {
                                    binding.spPrice.setSelection(i)
                                }
                            }
                            inflateSpinner(acceptedHolidays, binding.spAcceptedHolidays, emp)
                            binding.spAcceptedHolidays.isEnabled = true
                            for (i in acceptedHolidays.indices) {
                                if (acceptedHolidays[i] == emp.emp_accepted_holidays) {
                                    binding.spAcceptedHolidays.setSelection(i)
                                }
                            }
                        } else if (value == items[2]) {
                            inflateSpinner(hourlyRate, binding.spPrice, emp)
                            for (i in hourlyRate.indices) {
                                if (hourlyRate[i] == emp.emp_salary) {
                                    binding.spPrice.setSelection(i)
                                }
                            }
                            binding.spAcceptedHolidays.isEnabled = false
                        }
                    }
                }

            }
    }
}