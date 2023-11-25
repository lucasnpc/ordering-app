package com.example.orderingapp.commons

suspend fun <T : Any> safeRequest(
    apiCall: suspend () -> T
): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall.invoke())
    } catch (exception: Exception) {
        ApiResult.Error(exception)
    }
}
