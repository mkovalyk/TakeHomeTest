package com.application.places

/**
 * Data class that is used to pass some resource to UI
 */
data class Resource<T>(val state: State, val value: T?, val error: Exception?) {
    fun isSuccess(): Boolean = state == State.SUCCESS && error == null
}

enum class State {
    ERROR, SUCCESS, LOADING
}
