package com.upt.cti.smartwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.upt.cti.smartwallet.model.MonthlyExpenses

class MainActivity : AppCompatActivity() {

    private lateinit var messageTextView : TextView
    private lateinit var incomeEditText : EditText
    private lateinit var expensesEditText : EditText
    private lateinit var monthEditText : EditText
    private lateinit var searchButton : Button
    private lateinit var updateButton : Button
    private lateinit var databaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        incomeEditText = findViewById(R.id.income_editText)
        expensesEditText = findViewById(R.id.expensed_editText)
        monthEditText = findViewById(R.id.month_editText)
        searchButton = findViewById(R.id.search_button)
        updateButton = findViewById(R.id.update_button)
        messageTextView = findViewById(R.id.message_textView)

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = database.reference

        updateButton.setOnClickListener {
            val month : String = monthEditText.text.toString()
            val income = incomeEditText.text.toString()
            val expenses = expensesEditText.text.toString()

            val monthlyExpenses = MonthlyExpenses(expenses.toDouble(), month, income.toDouble())

            databaseReference.child(month).setValue(monthlyExpenses)
        }

        searchButton.setOnClickListener {
            searchMonth(monthEditText.text.toString())
        }
    }

    private fun searchMonth(month : String) {
        var monthData: MonthlyExpenses?
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                monthData = dataSnapshot.child(month).getValue<MonthlyExpenses>()
                incomeEditText.setText(monthData?.income.toString())
                expensesEditText.setText(monthData?.expenses.toString())

                if (monthData == null) {
                    messageTextView.text = "No records found"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Firebase", "databaseError ${databaseError.message}")
            }
        }
        databaseReference.addValueEventListener(listener)
    }
}