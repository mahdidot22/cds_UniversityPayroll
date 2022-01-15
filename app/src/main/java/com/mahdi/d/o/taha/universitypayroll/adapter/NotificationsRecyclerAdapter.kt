package com.mahdi.d.o.taha.universitypayroll.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdi.d.o.taha.universitypayroll.databinding.NotificationItemBinding
import com.mahdi.d.o.taha.universitypayroll.model.Emp
import com.mahdi.d.o.taha.universitypayroll.model.Notification

class NotificationsRecyclerAdapter(val context: Context, var list: ArrayList<Notification>) :
    RecyclerView.Adapter<NotificationsRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            setViews(list[position], binding)
        }
    }

    fun filterList(filterllist: ArrayList<Notification>) {
        list = filterllist
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun setViews(notification: Notification, binding: NotificationItemBinding) {
        with(notification) {
            binding.apply {
                title.text = notification.title
                Msg.text = notification.msg
                sender.text = notification.sender
            }
        }
    }

}