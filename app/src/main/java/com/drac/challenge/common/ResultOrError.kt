package com.drac.challenge.common

sealed class ResultOrError<out T : Any,out E: Throwable> {
    data class Fail<out E : Throwable>(val p0: Throwable) : ResultOrError<Nothing, E>()
    data class Result<out T: Any>(val p0: T) : ResultOrError<T, Nothing>()
}
