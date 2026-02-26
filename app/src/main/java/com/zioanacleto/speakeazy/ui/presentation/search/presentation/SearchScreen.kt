package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.domain.main.model.DrinkModel
import com.zioanacleto.speakeazy.core.domain.search.model.QueryModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchItem
import com.zioanacleto.speakeazy.core.domain.search.model.SearchLandingModel
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.FadeAndSlideAnimatedVisibility
import com.zioanacleto.speakeazy.ui.presentation.components.MainDrinkCard
import com.zioanacleto.speakeazy.ui.presentation.components.NewsBanner
import com.zioanacleto.speakeazy.ui.presentation.components.SearchFilterSection
import com.zioanacleto.speakeazy.ui.presentation.components.SearchInputText
import com.zioanacleto.speakeazy.ui.presentation.components.SelectedFilter
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onCocktailClick: (String) -> Unit
) {
    SearchScreenContent(modifier, onCocktailClick)
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier,
    onCocktailClick: (String) -> Unit
) {
    val viewModel: SearchViewModel = koinViewModel()

    val landingState by viewModel.landingUiState.collectAsState()

    when (landingState) {
        is SearchLandingUiState.Success -> {
            SearchScreenWithFilter(
                modifier = modifier,
                data = (landingState as SearchLandingUiState.Success).data,
                onButtonSearchClick = { isAiModeSearch, query ->
                    viewModel.search(isAiModeSearch, query)
                    viewModel.addQueryToDatabase(query)
                },
                onCocktailClick = onCocktailClick
            )
        }

        is SearchLandingUiState.Error -> {
            Text(
                modifier = Modifier
                    .padding(top = 10.dp),
                color = Color.White,
                text = "Error"
            )
        }

        is SearchLandingUiState.Loading -> {
            CocktailLoadingAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            )
        }
    }
}

// todo: maybe create a single UiState with both Filter and Search models
@Composable
private fun SearchScreenWithFilter(
    modifier: Modifier = Modifier,
    data: SearchLandingModel,
    onButtonSearchClick: (Boolean, String) -> Unit,
    onCocktailClick: (String) -> Unit
) {
    val viewModel: SearchViewModel = koinViewModel()
    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()
    val filterState = viewModel.filterUiState.collectAsStateWithLifecycle()
    val queryState = viewModel.queryUiState.collectAsStateWithLifecycle()
    val queryTextState = remember { mutableStateOf(TextFieldValue("")) }
    var selectedSearchFilterItem: SearchFilterItem? by remember { mutableStateOf(null) }
    var selectedFilters by remember {
        mutableStateOf<Map<SearchFilterItem, List<SelectedFilter>>>(emptyMap())
    }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    var isAiSearchMode by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hideKeyboardOnTouch()
                .verticalScroll(scrollState)
                .padding(top = 60.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 10.dp),
                text = "Search cocktails",
                color = Color.White,
                fontSize = TextUnit(36f, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold,
                lineHeight = TextUnit(42f, TextUnitType.Sp)
            )

            NewsBanner(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Try our AI assistant, tap on the input field's leading icon!",
                timed = true
            )

            SearchInputText(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                queryTextState = queryTextState.value,
                onQueryChange = { queryTextState.value = it },
                onTextFieldFocused = { isTextFieldFocused = it },
                onLeadingIconClick = { isAiSearchMode = it },
                onButtonSearchClick
            )

            SearchFilterSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 2.dp),
                list = listOf(SearchFilterItem.Ingredients, SearchFilterItem.Tags),
                onFilterSelectClick = { searchFilterItem ->
                    val filters = if (searchFilterItem == selectedSearchFilterItem)
                        selectedFilters[searchFilterItem].orEmpty()
                    else when (searchFilterItem) {
                        is SearchFilterItem.Ingredients -> {
                            data.ingredients.map {
                                SelectedFilter(it.name, false)
                            }
                        }

                        is SearchFilterItem.Tags -> {
                            data.tags.map {
                                SelectedFilter(it.name, false)
                            }
                        }

                        else -> listOf()
                    }

                    selectedSearchFilterItem = searchFilterItem
                    filters
                },
                onFilterDoneClick = { map ->
                    selectedFilters = map
                    viewModel.filter(selectedFilters)
                }
            )

            FadeAndSlideAnimatedVisibility(isTextFieldFocused) {
                LastQueriesSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .imePadding(),
                    lastQueries = data.lastQueries
                ) {
                    queryTextState.value = TextFieldValue(it)
                    isTextFieldFocused = false
                    focusManager.clearFocus()
                    onButtonSearchClick(isAiSearchMode, it)
                }
            }

            when {
                // query success result is displayed
                queryState.value is SearchUiState.Success ->
                    SearchSuccessView(
                        convertedModel = {
                            (queryState.value as SearchUiState.Success).search.results.map {
                                with(it as SearchItem.Cocktail) {
                                    DrinkModel(
                                        id = id,
                                        name = name,
                                        imageUrl = imageUrl,
                                        category = category,
                                        favorite = favorite,
                                        username = username.default()
                                    )
                                }
                            }
                        },
                        onCocktailClick = onCocktailClick
                    )
                // query error result is displayed as error message
                queryState.value is SearchUiState.Error ->
                    Text(
                        modifier = Modifier
                            .padding(top = 40.dp),
                        text = "No results found by query search.",
                        color = Color.White,
                        fontSize = TextUnit(28f, TextUnitType.Sp),
                        textAlign = TextAlign.Center
                    )
                // filter success result is displayed
                filterState.value is FilterUiState.Success ->
                    SearchSuccessView(
                        convertedModel = {
                            (filterState.value as FilterUiState.Success).filter.drinks
                        },
                        onCocktailClick = onCocktailClick
                    )
                // filter error result is displayed as error message
                filterState.value is FilterUiState.Error ->
                    Text(
                        modifier = Modifier
                            .padding(top = 40.dp),
                        text = "No results found by filter search.",
                        color = Color.White,
                        fontSize = TextUnit(28f, TextUnitType.Sp),
                        textAlign = TextAlign.Center
                    )
                // loading state for both queries is displayed
                queryState.value is SearchUiState.Loading || filterState.value is FilterUiState.Loading ->
                    CocktailLoadingAnimation(
                        modifier = Modifier
                            .size(400.dp)
                    )
            }
        }
    }
}

@Composable
private fun LastQueriesSection(
    modifier: Modifier = Modifier,
    lastQueries: List<QueryModel>,
    onQueryClicked: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        lastQueries.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 36.dp, end = 24.dp, bottom = 12.dp)
                    .clickable { onQueryClicked(it.query) },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = it.query,
                    color = Color.White,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
                Icon(
                    painter = rememberVectorPainter(Icons.Rounded.Clear),
                    contentDescription = "Delete query",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun SearchSuccessView(
    convertedModel: () -> List<DrinkModel>,
    onCocktailClick: (String) -> Unit
) {
    val drinks = convertedModel()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 16.dp, end = 16.dp)
    ) {
        drinks.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { drink ->
                    MainDrinkCard(
                        modifier = Modifier.weight(1f),
                        id = drink.id,
                        name = drink.name,
                        category = drink.category,
                        imageString = drink.imageUrl,
                        isFavorite = drink.favorite,
                        userName = drink.username,
                        onClick = onCocktailClick
                    )
                }

                // If only one item is available, we fill remaining space
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreenContent {}
}