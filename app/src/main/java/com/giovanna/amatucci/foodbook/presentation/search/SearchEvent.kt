package com.giovanna.amatucci.foodbook.presentation.search

sealed class SearchEvent {
    data class OnSearchQueryChange(val query: String) : SearchEvent()
}