package kz.sira.app.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.common.ToastHelper
import kotlin.reflect.KClass

sealed class State {
    object Loading : State()
    object Success : State()
    object Offline : State()
    object Error : State()
    object Empty : State()
    data class Custom(val key: String) : State()
}

open class QarBaseViewModel(get: Any) : ViewModel() {
    val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    protected fun setRefreshing(value: Boolean) {
        _isRefreshing.value = value
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy.asStateFlow()

    private val _canLoadMore = MutableStateFlow(false)
    open val canLoadMore: StateFlow<Boolean> = _canLoadMore.asStateFlow()

    private val _canStateChange = MutableStateFlow(false)
    val canStateChange: StateFlow<Boolean> = _canStateChange.asStateFlow()

    private val _currentState = MutableStateFlow<State>(State.Loading)
    val currentState: StateFlow<State> = _currentState.asStateFlow()

    private val _customStateKey = MutableStateFlow<String>("")
    val customStateKey: StateFlow<String> = _customStateKey.asStateFlow()

    private var isNavigating = false

    public fun navigateToActivity(
        context: Context,
        targetActivity: KClass<*>,
        paraDic: Map<String, Any>? = null,
        clearTask: Boolean = false
    ) {
        if (isNavigating) {
            return
        }
        isNavigating = true
        try {
            val intent = Intent(context, targetActivity.java).apply {
                paraDic?.forEach { (key, value) ->
                    when (value) {
                        is Int -> putExtra(key, value)
                        is Boolean -> putExtra(key, value)
                        is String -> putExtra(key, value)
                        // Add more types as needed
                        else -> putExtra(key, value.toString()) // Fallback to string
                    }
                }
                if (clearTask) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            context.startActivity(intent)
        } finally {
            isNavigating = false
        }
    }

    protected fun navigateToBack(context: Context) {
        if (isNavigating) {
            return
        }
        val activity = context as? Activity ?: return // 确保 context 是 Activity 类型
        isNavigating = true
        try {
            if (activity.isTaskRoot) {
                ToastHelper.showMessage(context, "error", "Press back again to exit the app")
            } else {
                activity.finish()
            }
        } finally {
            isNavigating = false
        }
    }

    protected fun updateBusyStatus(isBusy: Boolean) {
        _isBusy.value = isBusy
    }
    protected fun updateState(newState: State) {
        _currentState.value = newState
    }

    protected fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    protected open fun handleError(e: Exception) {
        e.printStackTrace()
        _currentState.value = State.Error
    }

}