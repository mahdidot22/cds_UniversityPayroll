package com.mahdi.d.o.taha.universitypayroll.model

import android.content.Context
import android.content.DialogInterface
import android.view.Menu
import android.view.MenuInflater
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahdi.d.o.taha.universitypayroll.R
import com.mahdi.d.o.taha.universitypayroll.adapter.SpinnerAdapter

class Constants {
    fun showDialog(
        context: Context, mainIcon: Int, title: String, msg: String,
        positiveBtnText: String, negativeBtnText: String?,
        positiveBtnClickListener: DialogInterface.OnClickListener,
        negativeBtnClickListener: DialogInterface.OnClickListener?
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setIcon(mainIcon)
            .setMessage(msg)
            .setCancelable(true)
            .setPositiveButton(positiveBtnText, positiveBtnClickListener)
        if (negativeBtnText != null)
            builder.setNegativeButton(negativeBtnText, negativeBtnClickListener)
        val alert = builder.create()
        alert.show()
        return alert
    }

    fun prepareAdapter(
        adapter: RecyclerView.Adapter<*>,
        recyclerView: RecyclerView,
        context: Context
    ) {
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun createOptionMenu(
        menuInflater: MenuInflater,
        menu: Menu?,
        menuRes: Int,
    ): SearchView {
        menuInflater.inflate(menuRes, menu)
        return menu!!.findItem(R.id.search).actionView as SearchView
    }

}