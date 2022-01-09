package com.mahdi.d.o.taha.universitypayroll.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdi.d.o.taha.universitypayroll.admin.details.Details
import com.mahdi.d.o.taha.universitypayroll.admin.update.Update_Emp
import com.mahdi.d.o.taha.universitypayroll.databinding.EmpItemBinding
import com.mahdi.d.o.taha.universitypayroll.model.Emp

class EmployeesRecyclerAdapter(val context: Context, var list: ArrayList<Emp>) :
    RecyclerView.Adapter<EmployeesRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: EmpItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EmpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.empName.text = this.emp_name
                binding.empId.text = this.emp_id
                binding.empContractType.text = this.emp_contract_type
                binding.info.setOnClickListener {
                    context.startActivity(Intent(context, Details::class.java))
                }
                binding.update.setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            Update_Emp::class.java
                        ).putExtra("username", this.emp_name)
                    )
                }
                binding.delete.setOnClickListener {
                    //delete here
                    list.remove(this)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }

    fun filterList(filterllist: ArrayList<Emp>) {
        list = filterllist
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}