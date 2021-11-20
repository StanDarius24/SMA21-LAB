package com.example.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab.R
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import android.view.View
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.DatabaseReference
import android.widget.TextView
import com.example.model.ui.PaymentAdapter
import java.lang.Exception

import com.google.firebase.database.ChildEventListener

import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var databaseReference: DatabaseReference? = null
    private val payments: MutableList<Payment?> = ArrayList()

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

        // setup firebase
        val database =
            FirebaseDatabase.getInstance("https://smart-wallet-f28ef-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference = database.reference
        databaseReference!!.child("wallet").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                try {
                    val payment: Payment? = dataSnapshot.getValue(Payment::class.java)
                    if (payment != null) {
                        payment.timestamp = dataSnapshot.key
                        System.out.println(payment.toString())
                        if (!payments.contains(payment)) {
                            payments.add(payment)
                        }
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}