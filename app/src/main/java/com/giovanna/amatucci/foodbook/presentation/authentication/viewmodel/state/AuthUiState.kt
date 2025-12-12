package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state

import com.giovanna.amatucci.foodbook.presentation.ScreenState
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.util.constants.UiText
data class AuthState(
    override val status: ScreenStatus = ScreenStatus.Loading,
    val navigateToHome: Boolean = false,
    val error: UiText? = null
) : ScreenState