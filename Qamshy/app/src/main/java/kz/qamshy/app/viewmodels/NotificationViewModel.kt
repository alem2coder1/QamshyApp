package kz.qamshy.app.viewmodels

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.qamshy.app.common.JsonHelper
import kz.qamshy.app.models.site.NotificationModel
import kz.qamshy.app.models.site.PushItem
import kz.qamshy.app.ui.activities.MainActivity
import kz.sira.app.viewmodels.QarBaseViewModel

typealias GroupedNotification = Pair<String, List<PushItem>>
class NotificationViewModel(get: Any) : QarBaseViewModel() {

    private val _notificationList =
        MutableStateFlow<List<GroupedNotification>>(emptyList())
    val notificationList = _notificationList.asStateFlow()

    private val _isLoadingIndicator = MutableStateFlow(true)
    val isLoadingIndicator: StateFlow<Boolean> = _isLoadingIndicator.asStateFlow()

    init {
//        loadNotData()
    }

    fun navigateToVerification(context: Context) {
        navigateToActivity(
            context = context,
            targetActivity = MainActivity::class
        )
    }

    fun onBackButtonPressed(context: Context) {
        navigateToBack(context)
    }

    private var currentPageOffset = 0
    var isLoadingMore = false
        private set

    fun loadMoreData() {
        if (_allPushItems.value.size >= totalCount) {
            return
        }
        if (_isRefreshing.value || isLoadingMore) return
        isLoadingMore = true
        currentPageOffset += 1
//        loadNotData {
//            isLoadingMore = false
//        }
    }

    fun refreshData(newStatusList: Set<Int> = emptySet(), isEnabled: Boolean = false ) {
        if (_isRefreshing.value) return
        setRefreshing(true)
        currentPageOffset = 0
        _notificationList.value = emptyList()
//        loadNotData {
//            setRefreshing(false)
//        }

    }


    private var totalCount: Int = 0
    private val _allPushItems = MutableStateFlow<List<PushItem>>(emptyList())
//    fun loadNotData(onComplete: () -> Unit = {}) {
//        viewModelScope.launch {
//            _isLoadingIndicator.value = true
//            try {
//                val paraDic = mapOf(
//                    "pageSize" to 10,
//                    "pageOffset" to currentPageOffset
//                )
//                val result = APIHelper.queryAsync("/api/push/list", "POST", paraDic = paraDic)
//                result.fold(
//                    onSuccess = { ajaxMsg ->
//                        if (ajaxMsg.status.equals("success", ignoreCase = true)) {
//                            val notificationModel =
//                                JsonHelper.convertAnyToObject<NotificationModel>(ajaxMsg.data)
//                            totalCount = notificationModel?.totalCount!!
//
//                            notificationModel.dataList.let { newPushItems ->
//                                _allPushItems.value = if (currentPageOffset == 0) {
//                                    newPushItems
//                                } else {
//                                    _allPushItems.value + newPushItems
//                                }
//
//                            }
//                        }
//                    },
//                    onFailure = { exception ->
//                    }
//                )
//            } catch (e: Exception) {
//                // 异常处理
//            } finally {
//                _isLoadingIndicator.value = false
//                onComplete()
//            }
//        }
//    }



//    fun switchToken(context: Context, onComplete: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            val result = APIHelper.queryAsync("/api/user/switch", "GET")
//            result.fold(
//                onSuccess = { ajaxMsg ->
//                    if (ajaxMsg.status.equals("success", ignoreCase = true)) {
//                        val token = ajaxMsg.data.toString()
//
//                        kotlinx.coroutines.delay(1000L)
//                        onComplete(true)
//                    } else {
//                        withContext(Dispatchers.Main) {
//                            kz.aljet.app.common.ToastHelper.showMessage(
//                                context,
//                                ajaxMsg.status,
//                                ajaxMsg.message
//                            )
//                        }
//                        onComplete(false)
//                    }
//                },
//                onFailure = { exception ->
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
//                    }
//                    onComplete(false) // 失败
//                }
//            )
//        }
//    }
}