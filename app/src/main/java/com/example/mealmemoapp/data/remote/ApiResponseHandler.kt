package com.example.mealmemoapp.data.remote

import com.example.mealmemoapp.utilities.Result
import retrofit2.Response

abstract class ApiResponseHandler {

    protected suspend fun <R> fetchResult(call: suspend () -> Response<R>): Result<R> {
        try {
            val result = call()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) return Result.Success(body)
            }
            return Result.Error("Network call has failed for the following reason: " +
                    "${result.message()} ${result.code()}")
        } catch (e: Exception) {
            return Result.Error("Network call has failed for the following reason: " +
                    (e.localizedMessage ?: e.toString()))
        }
    }
}