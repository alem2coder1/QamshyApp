package kz.qamshy.app.common

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

enum class ButtonStatus {
    Enabled,
    Loading,
    Disabled
}


enum class WhereOption {
    From,
    To
}

enum class VerificationType {
    Register,
    Recover,
    Change_phone;

    override fun toString(): String {
        return name.lowercase().replace("_","")
    }
}

