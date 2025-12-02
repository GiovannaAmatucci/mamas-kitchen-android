package com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state

sealed interface SearchEvent {
    data class UpdateSearchQuery(val query: String) : SearchEvent
    data class SubmitSearch(val query: String) : SearchEvent
    data class RecentSearchClicked(val query: String) : SearchEvent
    data class ActiveChanged(val active: Boolean) : SearchEvent
    data object ClearSearchQuery : SearchEvent
    data object ClearSearchHistory : SearchEvent
}