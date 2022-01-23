package com.mahdi.d.o.taha.universitypayroll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahdi.d.o.taha.universitypayroll.admin.Employees_Mnagment
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityMainBinding
import com.mahdi.d.o.taha.universitypayroll.employee.Statistics
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var _dp: FirebaseFirestore? = null
    private val dp get() = _dp!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        _dp = Firebase.firestore
        _constants = Constants()
        setContentView(binding.root)
        binding.apply {
            pswVisiblity.setOnClickListener { pswHideOrView(psw) }
            btnLogin.setOnClickListener { checkFields(username, psw) }
        }
    }

    private fun checkFields(username: EditText, psw: EditText) {
        if (username.text.isNotEmpty() && psw.text.isNotEmpty()) {
            getUsers(username.text, psw.text)
        } else {
            constants.fillFields(binding)
        }
    }

    private fun getUsers(username: Editable, psw: Editable) {
        getAdmins(username, psw)
    }

    private fun getEmps(username: Editable, psw: Editable) {
        dp.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng").collection("emp").get()
            .addOnSuccessListener {
                if (it.isEmpty) constants.addingMsg(binding, "There is no emp!!")
                else {
                    var list = arrayListOf<Any>()
                    for (doc in it) {
                        val userName = doc.get("id")
                        val fullName = doc.get("fullName")
                        val userPsw = doc.get("psw")
                        val contractType = doc.get("contract")
                        val salary = doc.get("salary")
                        val year = doc.get("year")
                        val holidays = doc.get("acceptedHolidays")
                        val remaining = doc.get("remainingHolidays")
                        val payments = doc.get("payments")
                        if (userName == username.toString() && userPsw == psw.toString()) {
                            if (contractType == "Part-Time") {
                                list.add(
                                    Emp.Casual(
                                        fullName.toString(),
                                        userName.toString(),
                                        contractType.toString(),
                                        salary.toString(),
                                        year.toString(),
                                        payments.toString()
                                    )
                                )
                            } else if (contractType == "Full-Time") {
                                list.add(
                                    Emp.Full(
                                        fullName.toString(),
                                        userName.toString(),
                                        contractType.toString(),
                                        salary.toString(),
                                        year.toString(),
                                        holidays.toString(),
                                        remaining.toString(),
                                        payments.toString()
                                    )
                                )
                            }
                            saveUsername(fullName.toString())
                            startActivity(Intent(this@MainActivity, Statistics::class.java).putExtra("user",list).putExtra("type",contractType.toString()))
                            username.clear()
                            psw.clear()
                        } else {
                            Snackbar.make(
                                binding.root,
                                "Invalid username or password!!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }.addOnFailureListener {
                Snackbar.make(binding.root, "$it", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun getAdmins(username: Editable, psw: Editable) {
        dp.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng").collection("admin").get()
            .addOnSuccessListener {
                for (doc in it) {
                    val userName = doc.get("username")
                    val userPsw = doc.get("psw")
                    if (userName == username.toString() && userPsw == psw.toString()) {
                        saveUsername(username.toString())
                        startActivity(Intent(this@MainActivity, Employees_Mnagment::class.java))
                        username.clear()
                        psw.clear()
                    } else {
                        getEmps(username, psw)
                    }
                }
            }.addOnFailureListener {
                Snackbar.make(binding.root, "$it", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun pswHideOrView(psw: EditText) {
        if (psw.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
            psw.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            psw.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    private fun saveUsername(username: String) {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putString("username", username)
        myEdit.apply()
    }
}