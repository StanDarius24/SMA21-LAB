package com.example.model.ui

import android.widget.TextView

import android.widget.RelativeLayout

import com.example.model.Payment

import android.R

import android.app.Activity
import android.content.Context

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ArrayAdapter


class PaymentAdapter(
    context: Context,
    resource: Int,
    textViewResourceId: Int,
    objects: Array<out Payment>
) : ArrayAdapter<Payment>(context, resource, textViewResourceId, objects) {

    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val itemHolder: ItemHolder
        var view: View? = convertView
        if (view == null) {
            val inflater = (context as Activity?)!!.layoutInflater
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
        val (timestamp, cost, name, type) = payments!![position]
        itemHolder.tIndex!!.text = (position + 1).toString()
        itemHolder.tName!!.text = name
        itemHolder.lHeader!!.setBackgroundColor(PaymentType.getColorFromPaymentType(type))
        itemHolder.tCost!!.text = "$cost LEI"
        itemHolder.tType!!.text = type
        itemHolder.tDate!!.text = "Date: " + timestamp.substring(0, 10)
        itemHolder.tTime!!.text = "Time: " + timestamp.substring(11)
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
}