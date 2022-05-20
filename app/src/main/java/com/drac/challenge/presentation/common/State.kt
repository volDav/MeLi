package com.drac.challenge.presentation.common

sealed class State<T> {
    class Success<T>(val data: T) : State<T>()
    class Error<T>(val message: String, val code: Int = 0) : State<T>()
    class Loading<T> : State<T>()
    class Empty<T> : State<T>()
}