package com.example.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab.R
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.widget.Toast
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.FirebaseDatabase
import android.view.View
import com.google.firebase.database.DatabaseReference
import android.widget.Spinner
import android.widget.EditText
import android.widget.TextView
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var tStatus: TextView
    private lateinit var eIncome: EditText
    private lateinit var eExpenses: EditText
    private lateinit var sSearch: Spinner

    private lateinit var databaseReference: DatabaseReference

    private lateinit var currentMonth: String
    private lateinit var databaseListener: ValueEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tStatus = findViewById(R.id.tStatus)
        eIncome = findViewById(R.id.eIncome)
        eExpenses = findViewById(R.id.eExpenses)
        sSearch = findViewById(R.id.spinner)
        val database =
            FirebaseDatabase.getInstance("https://smart-wallet-f28ef-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference = database.reference

        val monthlyExpenses: MutableList<MonthlyExpenses> = ArrayList()
        val monthNames: MutableList<String?> = ArrayList()
        val sAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, monthNames
        )
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sSearch.adapter = sAdapter

        databaseReference.child("calendar").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (monthSnapshot in dataSnapshot.children) {
                    try {
                        val me = monthSnapshot.getValue(
                            MonthlyExpenses::class.java
                        )
                        if (me != null) {
                            me.month = monthSnapshot.key
                            if (!monthNames.contains(me.month)) {
                                monthlyExpenses.add(me)
                                monthNames.add(me.month)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                sAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        sSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                pos: Int, id: Long
            ) {
                currentMonth = parent.getItemAtPosition(pos).toString()
                createNewDBListener()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun clicked(view: View) {
        when (view.id) {
            R.id.bUpdate -> if (databaseReference != null && currentMonth != null) {
                updateDB()
            } else {
                Toast.makeText(this, "db reference/current month is null", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateDB() {
        val eIncomeText = eIncome.text.toString()
        val eExpensesText: String = eExpenses.text.toString()

        if (eIncomeText.isNotEmpty() && eExpensesText.isNotEmpty()) {
            try {
                val uIncome = eIncomeText.toDouble()
                val uExpenses = eExpensesText.toDouble()
                val currentMonth = sSearch.selectedItem.toString()

                databaseReference.child("calendar").child(currentMonth).child("income")
                    .setValue(uIncome)
                databaseReference.child("calendar").child(currentMonth).child("expenses")
                    .setValue(uExpenses)
                Toast.makeText(this, "Update successful", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Income and expenses must be in numeric format",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(this, "Income and expenses fields cannot be empty!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun createNewDBListener() {
        if (databaseReference != null && currentMonth != null && databaseListener != null) databaseReference.child(
            "calendar"
        ).child(
            currentMonth
        ).removeEventListener(databaseListener)
        databaseListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val monthlyExpense = dataSnapshot.getValue(
                    MonthlyExpenses::class.java
                )
                if (monthlyExpense != null) {
                    monthlyExpense.month = dataSnapshot.key
                    if (currentMonth == monthlyExpense.month) {
                        eIncome.setText(monthlyExpense.income.toString())
                        eExpenses.setText(monthlyExpense.expenses.toString())
                        tStatus.text = "Found entry for $currentMonth"
                    }
                } else {
                    tStatus.text = "No entry found for $currentMonth"
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        databaseReference.child("calendar").child(currentMonth)
            .addValueEventListener(databaseListener)
    }
}