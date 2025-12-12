package com.giovanna.amatucci.foodbook.presentation

import com.giovanna.amatucci.foodbook.util.constants.UiText

sealed interface ScreenStatus {
    data object Loading : ScreenStatus
    data object Success : ScreenStatus
    data class Error(val message: UiText) : ScreenStatus
}

interface ScreenState {
    val status: ScreenStatus
}