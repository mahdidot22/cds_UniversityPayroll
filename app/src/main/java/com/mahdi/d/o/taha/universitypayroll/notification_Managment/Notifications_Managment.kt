package com.mahdi.d.o.taha.universitypayroll.notification_Managment

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.EmployeesRecyclerAdapter
import com.mahdi.d.o.taha.universitypayroll.adapter.ItemSwipeDeleteAdapter
import com.mahdi.d.o.taha.universitypayroll.adapter.NotificationsRecyclerAdapter
import com.mahdi.d.o.taha.universitypayroll.databinding.ActivityNotificationsBinding
import com.mahdi.d.o.taha.universitypayroll.model.Constants
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import com.mahdi.d.o.taha.universitypayroll.model.Notification
import com.mahdi.d.o.taha.universitypayroll.model.UnderlayButton

class Notifications_Managment : AppCompatActivity() {
    private var _binding: ActivityNotificationsBinding? = null
    private val binding get() = _binding!!
    private var _constant: Constants? = null
    private val constants get() = _constant!!
    private var _db: FirebaseFirestore? = null
    private val db get() = _db!!
    lateinit var adapter: NotificationsRecyclerAdapter
    lateinit var list: ArrayList<Notification>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNotificationsBinding.inflate(layoutInflater)
        _constant = Constants()
        _db =Firebase.firestore
        setContentView(binding.root)
        binding.toolbar.title = ""
        list = arrayListOf()
        this.setSupportActionBar(binding.toolbar)
        binding.shimmerViewContainer.startShimmer()
        adapter = NotificationsRecyclerAdapter(this,list)
        notificationsList()
        binding.apply {
            toolbar.setNavigationOnClickListener {
                hideRecyclerView()
                list.clear()
                adapter.notifyDataSetChanged()
                notificationsList()
            }
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
                                deleteNotification(list[position].title, list[position].msg)
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

    private fun notificationsList() {
        sentNotifications()
        othersNotifications()
    }

    private fun sentNotifications(){
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val username = sh.getString("username", "")
        db.collection("payrollNotifications").whereEqualTo("sender", username)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    constants.addingMsg(binding, "No sent Notifications yet!")
                    adapter = NotificationsRecyclerAdapter(this, list)
                    constants.prepareAdapter(adapter, binding.notificationsList, this)
                    showRecyclerView()
                } else {
                    for (doc in it) {
                        val title = doc.getString("title")
                        val msg = doc.getString("content")
                        val receiver = doc.getString("receiver")


                        Thread {
                            Handler(Looper.getMainLooper()).postDelayed({

                                list.add(
                                    Notification(
                                        title.toString(),
                                        msg.toString(),
                                        "me to $receiver"
                                    )
                                )
                                adapter = NotificationsRecyclerAdapter(this, list)
                                constants.prepareAdapter(
                                    adapter,
                                    binding.notificationsList,
                                    this
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

    private fun othersNotifications(){
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val username = sh.getString("username", "")
        db.collection("payrollNotifications").whereEqualTo("receiver", username)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    constants.addingMsg(binding, "No received Notifications yet!")
                    adapter = NotificationsRecyclerAdapter(this, list)
                    constants.prepareAdapter(adapter, binding.notificationsList, this)
                    showRecyclerView()
                } else {
                    for (doc in it) {
                        val title = doc.getString("title")
                        val msg = doc.getString("content")
                        val sender = doc.getString("sender")

                        Thread {
                            Handler(Looper.getMainLooper()).postDelayed({

                                list.add(
                                    Notification(
                                        title.toString(),
                                        msg.toString(),
                                       "from $sender"
                                    )
                                )
                                adapter = NotificationsRecyclerAdapter(this, list)
                                constants.prepareAdapter(
                                    adapter,
                                    binding.notificationsList,
                                    this
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
        binding.notificationsList.visibility = View.VISIBLE
    }

    private fun hideRecyclerView() {
        binding.shimmerViewContainer.apply {
            startShimmer()
            visibility = View.VISIBLE
        }
        binding.notificationsList.visibility = View.GONE
    }

    private fun deleteNotification(title: String, msg: String) {
        db.collection("payrollNotifications")
            .whereEqualTo("title", title).whereEqualTo("content", msg).get().addOnSuccessListener {
                if (it.isEmpty) {
                    constants.toast(
                        this,
                        "MISSY ITEM! PLEASE RETRY AGAIN LATER".lowercase()
                    )
                } else {
                    for (doc in it) {
                        db.collection("payrollNotifications").document(doc.id).delete()
                    }
                }
            }.addOnFailureListener {
                constants.toast(this, it.localizedMessage!!)
            }
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