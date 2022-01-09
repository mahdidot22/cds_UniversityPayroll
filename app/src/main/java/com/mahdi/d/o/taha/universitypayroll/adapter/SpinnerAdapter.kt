package com.mahdi.d.o.taha.universitypayroll.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter(val context: Context, val items: Array<String>) {
    val spinnerAdapter = object :
        ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
        override fun isEnabled(position: Int): Boolean {
            return position != 0
        }

        override fun getDropDownView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            val view: TextView =
                super.getDropDownView(position, convertView, parent) as TextView
            if (position == 0) {
                view.setTextColor(Color.GRAY)
            }
            return view
        }
    }
}