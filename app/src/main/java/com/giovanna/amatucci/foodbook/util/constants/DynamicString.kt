package com.giovanna.amatucci.foodbook.util.constants

import android.content.Context
import androidx.annotation.StringRes

/**
 * A sealed class to handle strings that can be either a raw String or an Android Resource ID.
 * This allows ViewModels to pass UI text without holding a Context reference.
 */
sealed class UiText {
    data class DynamicString(val value: String) : UiText()

    /**
     * @param resId The string resource ID.
     * @param args Optional arguments for formatting the string resource.
     */
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

