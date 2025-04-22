package kz.qamshy.app.models

data class LanguageModel(
    val flagUrl:String,
    val fullName: String,
    val shortName: String,
    val languageCulture: String,
    val isDefault:Boolean,
    val uniqueSeoCode:String = "",
)