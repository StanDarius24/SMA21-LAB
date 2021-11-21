package com.example.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab.R
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ListView

import com.example.AppState

import com.google.firebase.database.DatabaseError


import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ChildEventListener

import com.google.firebase.database.FirebaseDatabase

import com.example.ui.PaymentAdapter

import com.google.android.material.floatingactionbutton.FloatingActionButton

import android.widget.TextView
import com.example.AddPayment

import com.google.firebase.database.DatabaseReference
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var databaseReference: DatabaseReference? = null
    private val payments: MutableList<Payment> = ArrayList()

    // ui
    var tStatus: TextView? = null
    var bPrevious: Button? = null
    var bNext: Button? = null
    var fabAdd: FloatingActionButton? = null
    var listPayments: ListView? = null
    var adapter: PaymentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        tStatus = findViewById(R.id.tStatus)
        bPrevious = findViewById(R.id.bPrevious)
        bNext = findViewById(R.id.bNext)
        fabAdd = findViewById(R.id.fabAdd)
        listPayments = findViewById(R.id.listPayments)
        adapter = PaymentAdapter(this, R.layout.payment_item, payments)
        listPayments.setAdapter(adapter)

        val database =
            FirebaseDatabase.getInstance("https://com-test-smaLab-f28ef-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference = database.reference
        AppState.get().setDatabaseReference(databaseReference)
        AppState.get().getDatabaseReference().child("wallet")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    try {
                        val payment = dataSnapshot.getValue(Payment::class.java)
                        if (payment != null) {
                            payment.timestamp = dataSnapshot.key
                            println(payment.toString())
                            if (!payments.contains(payment)) {
                                payments.add(payment)
                            }
                            adapter!!.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    try {
                        val payment = dataSnapshot.getValue(Payment::class.java)
                        if (payment != null) {
                            payment.timestamp = dataSnapshot.key
                            for (p in payments) {
                                if (p.timestamp == payment.timestamp) {
                                    payments[payments.indexOf(p)] = payment
                                    break
                                }
                            }
                            adapter!!.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    try {
                        val payment = dataSnapshot.getValue(Payment::class.java)
                        if (payment != null) {
                            payment.timestamp = dataSnapshot.key
                            for (p in payments) {
                                if (p == payment) {
                                    payments.remove(p)
                                    break
                                }
                            }
                            adapter!!.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun clicked(view: View) {
        when (view.getId()) {
            R.id.fabAdd -> {
                AppState.get().setCurrentPayment(null)
                startActivity(Intent(this, AddPayment::class.java))
            }
        }
    }

}