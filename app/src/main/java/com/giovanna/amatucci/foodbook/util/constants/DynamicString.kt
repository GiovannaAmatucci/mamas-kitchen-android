package com.giovanna.amatucci.foodbook.util.constants

import android.content.Context
import androidx.annotation.StringRes
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @param:StringRes val resId: Int, vararg val args: Int?
    ) : UiText()


    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }

    }
}

