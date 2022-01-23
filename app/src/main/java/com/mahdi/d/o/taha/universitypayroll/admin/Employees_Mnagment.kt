package com.mahdi.d.o.taha.universitypayroll.admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.EmployeesRecyclerAdapter
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityEmployeesManegmentBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import com.mahdi.d.o.taha.universitypayroll.notification_Managment.Notifications_Managment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Employees_Mnagment : AppCompatActivity() {
    private var _binding: ActivityEmployeesManegmentBinding? = null
    private val binding get() = _binding!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    private var _db: FirebaseFirestore? = null
    private val db get() = _db!!
    lateinit var adapter: EmployeesRecyclerAdapter
    lateinit var list: ArrayList<Emp.EpmJopInfo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmployeesManegmentBinding.inflate(layoutInflater)
        _constants = Constants()
        _db = Firebase.firestore
        setContentView(binding.root)
        list = arrayListOf()
        binding.shimmerViewContainer.startShimmer()
        empsList()
        binding.apply {
            toolbar.title = ""
            this@Employees_Mnagment.setSupportActionBar(toolbar)
            toolbar.setNavigationOnClickListener {
                hideRecyclerView()
                list.clear()
                adapter.notifyDataSetChanged()
                empsList()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        constants.createOptionMenu(menuInflater, menu, R.menu.managment_menu).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    filter(newText)
                    return false
                }
            })
            queryHint = "name,id or contract type..."
        }
        return true
    }

    private fun empsList() {
        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng").collection("emp")
            .orderBy("year", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    constants.addingMsg(binding, "No employees yet!")
                    adapter = EmployeesRecyclerAdapter(this, list)
                    constants.prepareAdapter(adapter, binding.empList, this)
                    showRecyclerView()
                } else {
                    for (doc in it) {
                        val fullName = doc.getString("fullName")
                        val contract = doc.getString("contract")
                        val id = doc.getString("id")
                        val acceptedHolidays = doc.getString("acceptedHolidays")
                        val remainingHolidays = doc.getString("remainingHolidays")
                        val salary = doc.getString("salary")
                        val year = doc.getString("year")
                        val payments = doc.getString("payments")

                        Thread {
                            Handler(Looper.getMainLooper()).postDelayed({

                                list.add(
                                    Emp.EpmJopInfo(
                                        Emp(fullName, id, contract),
                                        acceptedHolidays,
                                        remainingHolidays,
                                        salary,
                                        year,
                                        payments
                                    )
                                )
                                adapter = EmployeesRecyclerAdapter(this, list)
                                constants.prepareAdapter(
                                    adapter,
                                    binding.empList,
                                    this@Employees_Mnagment
                                )
                                showRecyclerView()
                            }, 3000)
                        }.start()
                    }
                }
            }
            .addOnFailureListener {
                showRecyclerView()
                constants.addingMsg(binding, it.localizedMessage!!)
            }
    }

    private fun showRecyclerView() {
        binding.shimmerViewContainer.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.empList.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        binding.shimmerViewContainer.apply {
            startShimmer()
            visibility = View.VISIBLE
        }
        binding.empList.visibility = View.GONE
    }

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_new_emp -> {
                startActivity(Intent(this, Add_Emp::class.java)); this.finish()
            }
            R.id.payForAll -> {
                val simpleDateFormat = SimpleDateFormat("MM")
                val currentMonth: String = simpleDateFormat.format(Date())
                constants.showDialog(
                    this,
                    R.drawable.ic_payment,
                    "Paid for all employees",
                    "Are you sure?",
                    "Close",
                    "Paid",
                    (DialogInterface.OnClickListener
                    { dialog, _ ->
                        run {
                            dialog.cancel()
                        }

                    }),
                    (DialogInterface.OnClickListener { dialog, _ ->
                        run {
                            list.forEach {
                                if (it.emp.emp_contract_type == "Part-Time") {
                                    partTimePayment(
                                        currentMonth,
                                        it.emp_salary!!.toInt(),
                                        it.emp.emp_id.toString()
                                    )
                                } else if (it.emp.emp_contract_type == "Full-Time") {
                                    fullTimePayment(
                                        currentMonth,
                                        it.emp_salary!!.toInt(),
                                        it.emp.emp_id.toString()
                                    )
                                }
                            }
                            dialog.dismiss()
                        }
                    })
                )
            }
            R.id.notifications -> startActivity(Intent(this, Notifications_Managment::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun partTimePayment(date: String, salary: Int, id: String) {
        when (salary) {
            5 -> {
                payment(date, salary, 20, id)
            }
            10 -> {
                payment(date, salary, 15, id)

            }
            20 -> {
                payment(date, salary, 10, id)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun fullTimePayment(date: String, salary: Int, id: String) {
        val payment = hashMapOf(
            "payment" to salary,
            "date" to date,
            "id" to id
        )
        db.collection("payrollPayments").document("GtJXHveLoafmkbhCnwPo").collection("fullTime")
            .whereEqualTo("date", date)
            .get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection("payrollPayments").document("GtJXHveLoafmkbhCnwPo")
                        .collection("fullTime").add(payment)
                        .addOnFailureListener {
                            constants.addingMsg(
                                binding,
                                "something went wrong! please try again..."
                            )
                        }.addOnSuccessListener {
                            constants.toast(this, "$id payment paid!")
                        }
                } else {
                    constants.toast(this, "$id payment paid before for this month")
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun payment(date: String, salary: Int, totalHours: Int, id: String) {
        val monthlyPayment = (salary * totalHours) * 4
        val payment = hashMapOf(
            "payment" to monthlyPayment,
            "date" to date,
            "id" to id
        )
        db.collection("payrollPayments").document("GtJXHveLoafmkbhCnwPo").collection("partTime")
            .whereEqualTo("date", date)
            .get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection("payrollPayments").document("GtJXHveLoafmkbhCnwPo")
                        .collection("partTime").add(payment)
                        .addOnFailureListener {
                            constants.addingMsg(
                                binding,
                                "something went wrong! please try again..."
                            )
                        }.addOnSuccessListener {
                            constants.toast(this, "$id payment paid!")
                        }
                } else {
                    constants.toast(this, "$id payment paid before for this month")
                }
            }
    }

    private fun filter(newText: String?) {
        val filteredList = ArrayList<Emp.EpmJopInfo>()
        list.forEach {
            when {
                it.emp.emp_name.toString().lowercase()
                    .contains(newText!!.lowercase()) -> filteredList.add(it)
                it.emp.emp_id.toString().lowercase()
                    .contains(newText.lowercase()) -> filteredList.add(it)
                it.emp.emp_contract_type.toString().lowercase()
                    .contains(newText.lowercase()) -> filteredList.add(
                    it
                )
            }
        }
        if (filteredList.isEmpty()) {
            adapter.filterList(ArrayList())
            binding.empList.setBackgroundResource(R.drawable.no_data)
        } else {
            adapter.filterList(filteredList)
            binding.empList.setBackgroundResource(R.color.secondaryColor)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}