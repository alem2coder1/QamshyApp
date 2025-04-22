package kz.qamshy.app.models

sealed class OrderUiState<out T> {
    object Loading : OrderUiState<Nothing>()
    data class Success<out T>(val data: T) : OrderUiState<T>()
    data class Error(val message: String) : OrderUiState<Nothing>()
}