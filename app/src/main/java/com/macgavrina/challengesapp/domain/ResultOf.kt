package com.macgavrina.challengesapp.domain

sealed class ResultOf<T : Any> {
    class Success<T : Any>(val data: T) : ResultOf<T>()
    class Error<T : Any>(val message: String) : ResultOf<T>()
}