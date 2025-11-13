package com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state

sealed interface DetailsStatus {
    data object Loading : DetailsStatus
    data object Success : DetailsStatus
    data object Error : DetailsStatus
}