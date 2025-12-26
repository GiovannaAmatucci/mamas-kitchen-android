package com.giovanna.amatucci.foodbook.util.constants

import androidx.annotation.StringRes

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(@param:StringRes val resId: Int) : UiText()
}

