package com.mahdi.d.o.taha.universitypayroll.admin.employees_Managment

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.EmployeesRecyclerAdapter
import com.mahdi.d.o.taha.universitypayroll.admin.notification_Managment.Notifications_Managment
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityEmployeesManegmentBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import kotlin.collections.ArrayList


class Employees_Mnagment : AppCompatActivity() {
    private var _binding: ActivityEmployeesManegmentBinding? = null
    private val binding get() = _binding!!
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    lateinit var adapter: EmployeesRecyclerAdapter
    lateinit var list: ArrayList<Emp>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmployeesManegmentBinding.inflate(layoutInflater)
        _constants = Constants()
        setContentView(binding.root)
        list = arrayListOf()
        list.add(Emp("mahdi", "1111", "Full-time"))
        list.add(Emp("mahdi", "2222", "Part-time"))
        adapter = EmployeesRecyclerAdapter(this, list)
        binding.apply {
            toolbar.title = ""
            this@Employees_Mnagment.setSupportActionBar(toolbar)
            constants.prepareAdapter(adapter, binding.empList, this@Employees_Mnagment)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_new_emp -> startActivity(Intent(this, Add_Emp::class.java))
            R.id.payForAll -> {
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
                            dialog.dismiss()
                        }
                    })
                )
            }
            R.id.notifications -> startActivity(Intent(this, Notifications_Managment::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun filter(newText: String?) {
        val filteredList = ArrayList<Emp>()
        list.forEach {
            when {
                it.emp_name.lowercase().contains(newText!!.lowercase()) -> filteredList.add(it)
                it.emp_id.lowercase().contains(newText.lowercase()) -> filteredList.add(it)
                it.emp_contract_type.lowercase().contains(newText.lowercase()) -> filteredList.add(
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