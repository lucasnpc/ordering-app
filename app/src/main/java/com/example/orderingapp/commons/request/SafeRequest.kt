package com.example.orderingapp.commons.request

suspend fun <T : Any> safeRequestSuspend(
    apiCall: suspend () -> T
): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall.invoke())
    } catch (exception: Exception) {
        ApiResult.Error(exception)
    }
}

fun <T : Any> safeRequest(
    apiCall: () -> T
): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall.invoke())
    } catch (exception: Exception) {
        ApiResult.Error(exception)
    }
}
