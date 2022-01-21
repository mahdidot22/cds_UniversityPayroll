package com.mahdi.d.o.taha.universitypayroll.notification_Managment

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.ItemSwipeDeleteAdapter
import com.mahdi.d.o.taha.universitypayroll.adapter.NotificationsRecyclerAdapter
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityNotificationsBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Notification
import com.mahdi.d.o.taha.universitypayroll.model.UnderlayButton

class Notifications_Managment : AppCompatActivity() {
    private var _binding: ActivityNotificationsBinding? = null
    private val binding get() = _binding!!
    private var _constant: Constants? = null
    private val constants get() = _constant!!
    lateinit var adapter: NotificationsRecyclerAdapter
    lateinit var list: ArrayList<Notification>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNotificationsBinding.inflate(layoutInflater)
        _constant = Constants()
        setContentView(binding.root)
        binding.toolbar.title = ""
        list = arrayListOf()
        this.setSupportActionBar(binding.toolbar)
        list.add(Notification("My payment", "I am asking for my payment", "mahdi"))
        list.add(Notification("My Ex", "how about my jop today", "fuad"))
        list.add(Notification("Ahmed Exam", "الحمد لله أنا سريع الكتابة باللغتين", "samar"))
        list.add(Notification("Recommendation", "he talk about arabic and eng", "besan"))
        adapter = NotificationsRecyclerAdapter(this, list)
        binding.apply {
            constants.prepareAdapter(adapter, notificationsList, this@Notifications_Managment)
            fBtnSend.setOnClickListener {
                startActivity(Intent(this@Notifications_Managment, Add_Notification::class.java))
            }
        }

        val itemTouchHelper =
            ItemTouchHelper(object : ItemSwipeDeleteAdapter(binding.notificationsList) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    val deleteBtn = deleteBtn(position)
                    return listOf(deleteBtn)
                }
            })
        itemTouchHelper.attachToRecyclerView(binding.notificationsList)
    }

    private fun deleteBtn(position: Int): UnderlayButton {
        return UnderlayButton(
            this,
            "Remove",
            14.0f,
            android.R.color.holo_red_light,
            object : ItemSwipeDeleteAdapter.UnderlayButtonClickListener {
                override fun onClick() {
                    constants.showDialog(
                        this@Notifications_Managment,
                        R.drawable.ic_delete,
                        "Delete notification",
                        "This will permanently delete it!",
                        "delete",
                        "close",
                        (DialogInterface.OnClickListener
                        { dialog, _ ->
                            run {
                                list.removeAt(position)
                                adapter.notifyItemRemoved(position)
                                dialog.dismiss()
                            }

                        }),
                        (DialogInterface.OnClickListener { dialog, _ ->
                            run {
                                dialog.cancel()
                            }
                        })
                    )
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        constants.createOptionMenu(menuInflater, menu, R.menu.notification_menu).run {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    filter(newText)
                    return false
                }
            })
            queryHint = "title,message or sender..."
        }

        return true
    }


    private fun filter(newText: String?) {
        val filteredList = ArrayList<Notification>()
        list.forEach {
            when {
                it.title.lowercase().contains(newText!!.lowercase()) -> filteredList.add(it)
                it.msg.lowercase().contains(newText.lowercase()) -> filteredList.add(it)
                it.sender.lowercase().contains(newText.lowercase()) -> filteredList.add(
                    it
                )
            }
        }
        if (filteredList.isEmpty()) {
            adapter.filterList(ArrayList())
            binding.notificationsList.setBackgroundResource(R.drawable.no_data)
        } else {
            adapter.filterList(filteredList)
            binding.notificationsList.setBackgroundResource(R.color.design_default_color_background)
        }
    }
}