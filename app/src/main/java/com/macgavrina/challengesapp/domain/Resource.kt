package com.macgavrina.challengesapp.domain

sealed class Resource<T : Any> {
    class Success<T : Any>(val data: T) : Resource<T>()
    class Error<T : Any>(val message: String) : Resource<T>()
}