package com.example

import com.example.model.Payment

import com.google.firebase.database.DatabaseReference
import java.text.SimpleDateFormat
import java.util.*


class AppState {

    companion object {
        private var singletonObject: AppState? = null

        @Synchronized
        fun get(): AppState? {
            if (singletonObject == null) {
                singletonObject = AppState()
            }
            return singletonObject
        }

        private var databaseReference: DatabaseReference? = null
        private var currentPayment: Payment? = null

        fun getDatabaseReference(): DatabaseReference? {
            return databaseReference
        }

        fun setDatabaseReference(databaseReference: DatabaseReference?) {
            this.databaseReference = databaseReference
        }

        fun setCurrentPayment(currentPayment: Payment?) {
            this.currentPayment = currentPayment
        }

        fun getCurrentPayment(): Payment? {
            return currentPayment
        }

        fun getCurrentTimeDate(): String? {
            val sdfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val now = Date()
            return sdfDate.format(now)
        }
    }
}