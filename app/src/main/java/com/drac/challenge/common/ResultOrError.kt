package com.drac.challenge.common

sealed class ResultOrError<out T : Any,out E: Exception> {
    data class Fail<out E : Exception>(val p0: Exception) : ResultOrError<Nothing, E>()
    data class Result<out T: Any>(val p0: T) : ResultOrError<T, Nothing>()
}
