package com.example.ui

import android.graphics.Color
import java.util.*

class PaymentType() {
    companion object {
        fun getColorFromPaymentType(type: String): Int {
            var type = type
            type = type.lowercase(Locale.getDefault())
            return if (type == "entertainment") Color.rgb(
                200,
                50,
                50
            ) else if (type == "food") Color.rgb(50, 150, 50) else if (type == "taxes") Color.rgb(
                20,
                20,
                150
            ) else if (type == "travel") Color.rgb(230, 140, 0) else Color.rgb(100, 100, 100)
        }

        fun getTypes(): Array<String>? {
            return arrayOf(
                "entertainment",
                "food",
                "taxes",
                "travel"
            )
        }
    }
}
