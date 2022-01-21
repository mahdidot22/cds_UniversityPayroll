package com.mahdi.d.o.taha.universitypayroll.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.admin.Employee_Details
import com.mahdi.d.o.taha.universitypayroll.admin.Employees_Mnagment
import com.mahdi.d.o.taha.universitypayroll.admin.Update_Emp
import com.mahdi.d.o.taha.universitypayroll.databinding.EmpItemBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import java.util.*
import kotlin.collections.ArrayList

class EmployeesRecyclerAdapter(
    val context: Context,
    var list: ArrayList<Emp.EpmJopInfo>,
) :
    RecyclerView.Adapter<EmployeesRecyclerAdapter.ViewHolder>() {
    private var _constants: Constants? = null
    private val constants get() = _constants!!
    private var _db: FirebaseFirestore? = null
    private val db get() = _db!!
    private val activity = context as Employees_Mnagment


    inner class ViewHolder(val binding: EmpItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EmpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        _constants = Constants()
        _db = Firebase.firestore
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            setViews(list[position], binding, adapterPosition)
        }
    }

    fun filterList(filterllist: ArrayList<Emp.EpmJopInfo>) {
        list = filterllist
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setViews(empInfo: Emp.EpmJopInfo, binding: EmpItemBinding, adapterPosition: Int) {
        with(empInfo) {
            binding.apply {
                binding.empName.text = emp.emp_name
                binding.empId.text = emp.emp_id
                binding.empContractType.text = emp.emp_contract_type
                binding.info.setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            Employee_Details::class.java
                        ).putExtra("emp", empInfo)
                    )
                }
                binding.update.setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            Update_Emp::class.java
                        ).putExtra("emp", empInfo)
                    )
                    activity.finish()
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
                                deleteEmp(emp.emp_id.toString())
                                dialog.cancel()
                                list.remove(empInfo)
                                notifyItemRemoved(adapterPosition)
                            }
                        })
                    )
                }
            }
        }

    }

    private fun deleteEmp(id: String) {
        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng").collection("emp")
            .whereEqualTo("id", id).get().addOnSuccessListener {
                if (it.isEmpty) {
                    constants.toast(
                        context,
                        "MISSY ID! PLEASE RETRY AGAIN LATER".lowercase()
                    )
                } else {
                    for (doc in it) {
                        db.collection("payrollUsers").document("fxYCtD3ZCFLQmra6tJng")
                            .collection("emp").document(doc.id).delete()
                    }
                }
            }.addOnFailureListener {
                constants.toast(context, it.localizedMessage!!)
            }
    }
}