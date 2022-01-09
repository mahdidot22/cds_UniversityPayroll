package com.mahdi.d.o.taha.universitypayroll.admin

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.EmployeesRecyclerAdapter
import com.mahdi.d.o.taha.universitypayroll.admin.add.Add_Emp
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityEmployeesManegmentBinding
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import java.util.*
import java.util.Locale.filter
import kotlin.collections.ArrayList
import kotlin.math.E

class EmployeesManagement : AppCompatActivity() {
    private var _binding: ActivityEmployeesManegmentBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: EmployeesRecyclerAdapter
    lateinit var list: ArrayList<Emp>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEmployeesManegmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list = ArrayList<Emp>()
        list.add(
            Emp(
                "Mahdi Taha", "1111", "Full-Time",
                "10", "5", "3000", "16/10/2023",
                "365", "8000"
            )
        )
        list.add(
            Emp(
                "Ahmed Mohammed", "2222", "Part-Time",
                "10", "5", "3000", "16/10/2023",
                "365", "8000"
            )
        )
        list.add(
            Emp(
                "Fuad Osama", "3333", "Full-Time",
                "10", "5", "3000", "16/10/2023",
                "365", "8000"
            )
        )
        list.add(
            Emp(
                "Omar Osman", "4444", "Full-Time",
                "10", "5", "3000", "16/10/2023",
                "365", "8000"
            )
        )
        list.add(
            Emp(
                "Orhan Sultan", "5555", "Part-Time",
                "10", "5", "3000", "16/10/2023",
                "365", "8000"
            )
        )
        adapter = EmployeesRecyclerAdapter(this, list)
        binding.empList.adapter = adapter
        binding.empList.setHasFixedSize(true)
        binding.empList.layoutManager = LinearLayoutManager(this)
        binding.addNewEmp.setOnClickListener {
            startActivity(Intent(this, Add_Emp::class.java))
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return true
            }

        })

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