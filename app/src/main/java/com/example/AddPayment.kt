package com.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab.R
import com.example.model.Payment

import android.view.View
import android.widget.*
import com.example.ui.PaymentType
import java.io.Serializable

import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


class AddPayment : AppCompatActivity() {
    private lateinit var eName: EditText
    private lateinit var eCost: EditText
    private lateinit var tTimestamp: TextView
    private lateinit var bUpdate: Button
    private lateinit var bDelete: Button
    private lateinit var sType: Spinner

    private var payment: Payment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payment)

        //
        title = "Add or edit payment"

        // ui
        eName = findViewById<View>(R.id.eName) as EditText
        eCost = findViewById<View>(R.id.eCost) as EditText
        sType = findViewById<View>(R.id.sType) as Spinner
        tTimestamp = findViewById<View>(R.id.tTimestamp) as TextView

        // spinner adapter
        val types: Array<String>? = PaymentType.getTypes()
        val sAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, types!!
        )
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sType!!.adapter = sAdapter

        // initialize UI if editing
        payment = AppState.getCurrentPayment()
        if (payment != null) {
            eName.setText(payment!!.name)
            eCost.setText(java.lang.String.valueOf(payment!!.cost))
            tTimestamp!!.text = "Time of payment: " + payment!!.timestamp
            try {
                sType!!.setSelection(Arrays.asList(types).indexOf<Serializable?>(payment!!.type))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            tTimestamp!!.text = ""
        }
    }

    fun clicked(view: View) {
        when (view.getId()) {
            R.id.bUpdate -> if (payment != null) save(payment!!.timestamp) else AppState.getCurrentTimeDate()?.let {
                save(it)
            }
            R.id.bDelete -> if (payment != null) delete(payment!!.timestamp) else Toast.makeText(
                this,
                "Payment does not exist",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun save(timestamp: String) {
        val payment = Payment(
            null,
            eCost!!.text.toString().toDouble(),
            eName!!.text.toString(),
            sType!!.selectedItem.toString()
        )
        val map: MutableMap<String, Any> = HashMap()
        map["cost"] = payment.cost
        map["name"] = payment.name
        map["type"] = payment.type
        println("timestamp: $timestamp")
        System.out.println("name: " + payment.name)
        System.out.println("cost: " + payment.cost)
        System.out.println("type: " + payment.type)
        AppState.get().getDatabaseReference().child("wallet").child(timestamp).updateChildren(map)
        finish()
    }

    private fun delete(timestamp: String) {
        AppState.get().getDatabaseReference().child("wallet").child(timestamp).removeValue()
        finish()
    }
}