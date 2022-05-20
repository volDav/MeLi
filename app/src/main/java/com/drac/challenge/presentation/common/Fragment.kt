package com.drac.challenge.presentation.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.lang.Exception

fun Fragment.put(any : Any) {
    val arguments = this.arguments ?: Bundle()
    arguments.putString(any.javaClass.name, Gson().toJson(any))
    this.arguments = arguments
}

fun <T> Fragment.get(classOfT: Class<T>): T {
    val json = this.arguments?.getString(classOfT.name, "{}") ?: "{}"
    return try {
        Gson().fromJson(json, classOfT)
    } catch (e : Exception) {
        Gson().fromJson("{}", classOfT)
    }
}