package com.mahdi.d.o.taha.universitypayroll.admin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isNotEmpty
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.SpinnerAdapter
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityAddEmpBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.PasswordManager
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class Add_Emp : AppCompatActivity() {
    private var _binding: ActivityAddEmpBinding? = null
    private val binding get() = _binding!!
    private var _dp: FirebaseFirestore? = null
    private val db get() = _dp!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddEmpBinding.inflate(layoutInflater)
        _dp = Firebase.firestore
        _constants = Constants()
        setContentView(binding.root)
        val cotract_type_items = resources.getStringArray(R.array.contract_type_array)
        binding.apply {
            inflateSpinner(cotract_type_items, spNewContractType)
            btnSaveContract.setOnClickListener {
                if (spNewContractType.selectedItem != null && spPrice.selectedItem != null) {
                    validation(
                        edNewUsername,
                        spNewContractType.selectedItem.toString(),
                        spPrice.selectedItem.toString(),
                        "${spAcceptedHolidays.selectedItem}",
                        genderGroup.checkedRadioButtonId
                    )
                } else {
                    constants.toast(this@Add_Emp, "Please check your list choices!")
                }
            }
        }
    }

    private fun validation(
        userFullName: EditText,
        userContractType: String,
        salary: String,
        holidays: String,
        gender: Int
    ) {
        val userGender = if (gender == binding.male.id) {
            1
        } else {
            2
        }
        val fullName = userFullName.text.toString()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val calendar = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val date: String = dateFormat.format(calendar)
        val myPasswordManager = PasswordManager()
        val newPsw: String = myPasswordManager.generatePassword(
            isWithLetters = false,
            isWithUppercase = false,
            isWithNumbers = true,
            isWithSpecial = false,
            length = 12
        )
        val id = "$userGender$currentYear${fullName.substring(0, 2)}${
            newPsw.substring(
                startIndex = 0,
                endIndex = 2
            )
        }"
        if (fullName.isNotEmpty() && userContractType.isNotEmpty() && salary.isNotEmpty()) {
            binding.apply {
                if (spNewContractType.selectedItemPosition == 0 || spPrice.selectedItemPosition == 0 || spAcceptedHolidays.selectedItemPosition == 0) {
                    constants.toast(this@Add_Emp, "Please check your list choices!")
                } else if (spNewContractType.selectedItemPosition == 1) {
                    if (spPrice.selectedItemPosition == 0 || spAcceptedHolidays.selectedItemPosition == 0) {
                        constants.toast(this@Add_Emp, "Please check your list choices!")
                    } else {
                        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng")
                            .collection("emp").whereEqualTo("id", id).get()
                            .addOnSuccessListener {
                                if (it.isEmpty) {
                                    addFullTimeUser(
                                        id,
                                        fullName,
                                        newPsw,
                                        userContractType,
                                        salary,
                                        holidays,
                                        holidays,
                                        date.toString(),
                                    )
                                } else {
                                    constants.addingMsg(
                                        binding,
                                        "user added before!! please try again if you feel it's wrong!"
                                    )
                                }
                            }.addOnFailureListener {
                                constants.addingMsg(binding, it.localizedMessage!!)
                            }
                    }
                } else if (spNewContractType.selectedItemPosition == 2) {
                    if (spPrice.selectedItemPosition == 0) {
                        constants.toast(this@Add_Emp, "Please check your list choices!")
                    } else {
                        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng")
                            .collection("emp").whereEqualTo("id", id).get()
                            .addOnSuccessListener {
                                if (it.isEmpty) {
                                    addCasualUser(
                                        id,
                                        fullName,
                                        newPsw,
                                        userContractType,
                                        salary,
                                        date.toString(),
                                    )
                                } else {
                                    constants.addingMsg(
                                        binding,
                                        "user added before!! please try again if you feel it's wrong!"
                                    )
                                }
                            }.addOnFailureListener {
                                constants.addingMsg(binding, it.localizedMessage!!)
                            }
                    }
                }
            }
        } else {
            constants.fillFields(binding)
        }
    }

    private fun addFullTimeUser(
        id: String,
        userFullName: String,
        newPsw: String,
        userContractType: String,
        salary: String,
        holidays: String,
        remainingHolidays: String,
        year: String,
    ) {
        val user = hashMapOf(
            "id" to id,
            "psw" to newPsw,
            "fullName" to userFullName,
            "contract" to userContractType,
            "salary" to salary,
            "acceptedHolidays" to holidays,
            "remainingHolidays" to remainingHolidays,
            "year" to year,
            "payments" to 0f.toString()
        )
        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng")
            .collection("emp").add(user).addOnSuccessListener {
                constants.addingMsg(binding, "user add successfully")
                onBackPressed()
            }.addOnFailureListener {
                constants.addingMsg(binding, "user add failed!!")
            }
    }

    private fun addCasualUser(
        id: String,
        userFullName: String,
        newPsw: String,
        userContractType: String,
        salary: String,
        year: String,
    ) {
        val user = hashMapOf(
            "id" to id,
            "psw" to newPsw,
            "fullName" to userFullName,
            "contract" to userContractType,
            "salary" to salary,
            "year" to year,
            "payments" to 0f.toString()

        )
        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng")
            .collection("emp").add(user).addOnSuccessListener {
                constants.addingMsg(binding, "user add successfully")
                onBackPressed()
            }.addOnFailureListener {
                constants.addingMsg(binding, "user add failed!!")
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,Employees_Mnagment::class.java))
        this.finish()
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
                            View.VISIBLE.apply {
                                binding.hide.visibility = this
                                binding.spAcceptedHolidays.visibility = this
                            }
                            inflateSpinner(fixedPrice, binding.spPrice)
                            inflateSpinner(acceptedHolidays, binding.spAcceptedHolidays)
                        } else if (value == items[2]) {
                            inflateSpinner(hourlyRate, binding.spPrice)
                            View.INVISIBLE.apply {
                                binding.hide.visibility = this
                                binding.spAcceptedHolidays.visibility = this
                            }
                        }
                    }
                }

            }
    }
}