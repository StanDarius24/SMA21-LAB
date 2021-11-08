package com.upt.cti.smartwallet.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MonthlyExpenses(
    val expenses: Double,
    var month: String,
    val income: Double
) {
    constructor() : this(0.0, "Undefined", 0.0)
}

