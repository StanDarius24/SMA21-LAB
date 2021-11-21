package com.example.ui

import com.example.model.Payment
import android.widget.ArrayAdapter;
import android.widget.TextView
import android.widget.RelativeLayout
import android.content.Intent
import com.example.AppState
import android.app.Activity
import android.content.Context
import android.view.View
import com.example.lab.R
import android.R

import android.view.ViewGroup


class PaymentAdapter(context: Context, layoutResourceID: Int, payments: List<Payment>) :
    ArrayAdapter<Payment?>(context, layoutResourceID, payments) {
    private val context: Context
    private val payments: List<Payment>
    private val layoutResID: Int
    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemHolder: ItemHolder
        var view: View? = convertView
        if (view == null) {
            val inflater = (context as Activity).layoutInflater
            itemHolder = ItemHolder()
            view = inflater.inflate(layoutResID, parent, false)
            itemHolder.tIndex = view.findViewById(R.id.tIndex)
            itemHolder.tName = view.findViewById(R.id.tName)
            itemHolder.lHeader = view.findViewById(R.id.lHeader)
            itemHolder.tDate = view.findViewById(R.id.tDate)
            itemHolder.tTime = view.findViewById(R.id.tTime)
            itemHolder.tCost = view.findViewById(R.id.tCost)
            itemHolder.tType = view.findViewById(R.id.tType)
            view.setTag(itemHolder)
        } else {
            itemHolder = view.getTag()
        }
        val pItem = payments[position]
        itemHolder.tIndex!!.text = (position + 1).toString()
        itemHolder.tName.setText(pItem.getName())
        itemHolder.lHeader!!.setBackgroundColor(PaymentType.getColorFromPaymentType(pItem.getType()))
        itemHolder.tCost.setText(pItem.getCost().toString() + " LEI")
        itemHolder.tType.setText(pItem.getType())
        itemHolder.tDate!!.text = "Date: " + pItem.timestamp.substring(0, 10)
        itemHolder.tTime!!.text = "Time: " + pItem.timestamp.substring(11)
        view.setOnClickListener(object : OnClickListener() {
            fun onClick(view: View?) {
//                Toast.makeText(context, "Clicked on " + position, Toast.LENGTH_SHORT).show();
                AppState.get().setCurrentPayment(payments[position])
                context.startActivity(Intent(context, AddPaymentActivity::class.java))
            }
        })
        return view
    }

    private class ItemHolder {
        var tIndex: TextView? = null
        var tName: TextView? = null
        var lHeader: RelativeLayout? = null
        var tDate: TextView? = null
        var tTime: TextView? = null
        var tCost: TextView? = null
        var tType: TextView? = null
    }

    init {
        this.context = context
        this.payments = payments
        layoutResID = layoutResourceID
    }
}