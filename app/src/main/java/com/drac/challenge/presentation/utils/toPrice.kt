package com.drac.challenge.presentation.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun toPrice(price: Long) : String {
    if(price <= 0) return "$ 0"

    val sym = DecimalFormatSymbols.getInstance()
    sym.groupingSeparator = '.'
    return try {
        DecimalFormat("$ ###,###", sym).format(price.toFloat())
    } catch (e: Exception) {
        "$ 0"
    }
}