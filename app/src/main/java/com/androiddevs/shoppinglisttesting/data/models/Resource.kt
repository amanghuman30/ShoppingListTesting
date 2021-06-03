package com.androiddevs.shoppinglisttesting.data.models

sealed class Resource<T>(
    status: Status,
    data : T? = null,
    message : String? = null
) {

    class Loading<T>() : Resource<T>(Status.LOADING)
    class Success<T>(data: T?, message: String?) : Resource<T>(Status.SUCCESS, data, message)
    class Error<T>(message: String?) : Resource<T>(Status.ERROR,null, message)
}

enum class Status {
    SUCCESS,
    LOADING,
    ERROR
}
