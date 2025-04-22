package kz.qamshy.app.models.site

import com.squareup.moshi.Json

data class PushItem(
    val title: String,
    val body: String,
    val pushDateUnixTime: Long,
    val pushTime: Long
)

data class NotificationModel(
    val totalCount: Int,
    val dataList: List<PushItem>
)
