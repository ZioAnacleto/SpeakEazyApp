package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.ui.presentation.components.SelectedFilter
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.startWith
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _queryUiState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState.None)
    val queryUiState: StateFlow<SearchUiState> = _queryUiState.asStateFlow()

    private val _filterUiState: MutableStateFlow<FilterUiState> =
        MutableStateFlow(FilterUiState.None)
    val filterUiState: StateFlow<FilterUiState> = _filterUiState.asStateFlow()

    val landingUiState: StateFlow<SearchLandingUiState> =
        repository.getSearchLandingData()
            .mapResourceAsSearchLandingUiState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchLandingUiState.Loading
            )

    fun search(queryString: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            repository.submitQuery(queryString)
                .mapResourceAsSearchUiState()
                .onStart { emit(SearchUiState.Loading) }
                .collect {
                    _queryUiState.value = it
                }
        }
    }

    fun filter(selectedSearchFilterItem: SearchFilterItem, selectedFilters: List<SelectedFilter>) {
        val filters = selectedFilters.filter{ it.second }.map{ it.first }
        AnacletoLogger.mumbling(
            mumble = "selectedFilters: $selectedFilters, filters: $filters"
        )
        viewModelScope.launch(dispatcherProvider.io()) {
            repository.submitFilter(
                selectedSearchFilterItem,
                filters
            )
                .mapResourceAsFilterUiState()
                .onStart { emit(FilterUiState.Loading) }
                .collect {
                    _filterUiState.value = it
                }
        }
    }
}