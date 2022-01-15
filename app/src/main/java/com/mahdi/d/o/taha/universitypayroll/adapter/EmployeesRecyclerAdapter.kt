package com.mahdi.d.o.taha.universitypayroll.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.admin.employees_Managment.Employee_Details
import com.mahdi.d.o.taha.universitypayroll.admin.employees_Managment.Update_Emp
import com.mahdi.d.o.taha.universitypayroll.databinding.EmpItemBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp

class EmployeesRecyclerAdapter(
    val context: Context,
    var list: ArrayList<Emp>,
) :
    RecyclerView.Adapter<EmployeesRecyclerAdapter.ViewHolder>() {
    private var _constants: Constants? = null
    private val constants get() = _constants!!

    inner class ViewHolder(val binding: EmpItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EmpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        _constants = Constants()
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            setViews(list[position], binding, adapterPosition)
        }
    }

    fun filterList(filterllist: ArrayList<Emp>) {
        list = filterllist
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setViews(emp: Emp, binding: EmpItemBinding, adapterPosition: Int) {
        with(emp) {
            binding.apply {
                binding.empName.text = emp_name
                binding.empId.text = emp_id
                binding.empContractType.text = emp_contract_type
                binding.info.setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            Employee_Details::class.java
                        ).putExtra("emp", emp)
                    )
                }
                binding.update.setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            Update_Emp::class.java
                        ).putExtra("username", emp_name).putExtra("id", emp_id)
                    )
                }
                binding.delete.setOnClickListener {
                    //delete here
                    constants.showDialog(
                        context,
                        R.drawable.ic_delete,
                        "Delete an Employee Contract",
                        "This will permanently delete it!",
                        "Close",
                        "Delete",
                        (DialogInterface.OnClickListener { dialog, _ ->
                            run {
                                dialog.dismiss()
                            }
                        }), (DialogInterface.OnClickListener { dialog, _ ->
                            run {
                                dialog.cancel()
                                list.remove(emp)
                                notifyItemRemoved(adapterPosition)
                            }
                        })
                    )
                }
            }
        }

    }
}