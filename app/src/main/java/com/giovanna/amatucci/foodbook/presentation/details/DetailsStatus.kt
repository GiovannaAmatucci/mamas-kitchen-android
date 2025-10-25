package com.giovanna.amatucci.foodbook.presentation.details

sealed interface DetailsStatus {
    data object Loading : DetailsStatus
    data object Success : DetailsStatus
    data object Error : DetailsStatus
}