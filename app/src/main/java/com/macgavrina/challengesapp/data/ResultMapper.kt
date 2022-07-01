package com.macgavrina.challengesapp.data

import com.macgavrina.challengesapp.domain.ResultOf
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> handleApiRequest(
    execute: suspend () -> Response<T>
): ResultOf<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ResultOf.Success(body)
        } else {
            ResultOf.Error(message = response.message())
        }
    } catch (e: HttpException) {
        ResultOf.Error(message = e.message())
    } catch (e: Exception) {
        ResultOf.Error(message = e.message ?: "Unknown error")
    }
}