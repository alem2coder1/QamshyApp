package kz.qamshy.app.models

data class AjaxMsgModel(
    val status: String,
    val message: String,
    val backUrl: String = "",
    val data: Any? = null
)