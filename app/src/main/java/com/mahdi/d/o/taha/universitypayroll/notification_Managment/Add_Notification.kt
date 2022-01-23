package com.mahdi.d.o.taha.universitypayroll.notification_Managment

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Spinner
import androidx.core.view.isNotEmpty
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahdi.d.o.taha.universitypayroll.adapter.EmployeesRecyclerAdapter
import com.mahdi.d.o.taha.universitypayroll.adapter.SpinnerAdapter
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityAddNotificationBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.content.SharedPreferences
import android.widget.ArrayAdapter


class Add_Notification : AppCompatActivity() {
    private var _binding: ActivityAddNotificationBinding? = null
    private var _db: FirebaseFirestore? = null
    private val binding get() = _binding!!
    private val db get() = _db!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    lateinit var list: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddNotificationBinding.inflate(layoutInflater)
        _db = Firebase.firestore
        _constants = Constants()
        list = arrayListOf()
        setContentView(binding.root)
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val username = sh.getString("username", "")
        usersList()
        binding.apply {
            tvSender.text = username
            btnSend.setOnClickListener {
                if (edMsg.text.isNotEmpty() && edTitle.text.isNotEmpty() && spReceiver.isNotEmpty()) {
                    if (list.size > 1) {
                        addNotification(
                            tvSender.text.toString(),
                            spReceiver.selectedItem.toString(),
                            edMsg.text.toString(),
                            edTitle.text.toString()
                        )
                    } else {
                        constants.addingMsg(binding, "there is no users to receive your massages!")
                    }
                } else {
                    constants.addingMsg(binding, "Fill Fields!!")
                }

            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun addNotification(sender: String, receiver: String, content: String, title: String) {
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        val notification = hashMapOf(
            "sender" to sender,
            "title" to title,
            "receiver" to receiver,
            "content" to content,
            "date" to currentDateAndTime
        )
        db.collection("payrollNotifications").add(notification).addOnSuccessListener {
            constants.addingMsg(binding, "sent successfully")
            onBackPressed()
        }.addOnFailureListener {
            constants.addingMsg(binding, "sent failed!!")
        }
    }

    private fun usersList() {
        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng").collection("emp")
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    constants.addingMsg(binding, "No employees yet!")
                } else {
                    for (doc in it) {
                        val fullName = doc.getString("fullName")
                        Thread {
                            Handler(Looper.getMainLooper()).postDelayed({
                                list.add(fullName.toString())
                                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                    this,
                                    R.layout.simple_spinner_dropdown_item,
                                    list
                                )
                                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                                binding.spReceiver.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }, 1000)
                        }.start()
                    }
                }
            }
            .addOnFailureListener {
                constants.addingMsg(binding, it.localizedMessage!!)
            }

        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng").collection("admin")
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    constants.addingMsg(binding, "No admins yet!")
                } else {
                    for (doc in it) {
                        val username = doc.getString("username")

                        Thread {
                            Handler(Looper.getMainLooper()).postDelayed({
                                list.plus(
                                    username.toString()
                                )
                            }, 3000)
                        }.start()
                    }
                }
            }
            .addOnFailureListener {
                constants.addingMsg(binding, it.localizedMessage!!)
            }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Notifications_Managment::class.java))
        this.finish()
    }
}