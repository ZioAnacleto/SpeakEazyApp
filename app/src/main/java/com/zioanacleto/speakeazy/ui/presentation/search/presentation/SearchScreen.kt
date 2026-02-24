package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.speakeazy.core.domain.search.model.QueryModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchItem
import com.zioanacleto.speakeazy.core.domain.search.model.SearchLandingModel
import com.zioanacleto.speakeazy.core.domain.search.model.SearchModel
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.MainDrinkCard
import com.zioanacleto.speakeazy.ui.presentation.components.NewsBanner
import com.zioanacleto.speakeazy.ui.presentation.components.SearchFilterSection
import com.zioanacleto.speakeazy.ui.presentation.components.SearchInputText
import com.zioanacleto.speakeazy.ui.presentation.components.SelectedFilter
import kotlinx.coroutines.delay
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
    val queryTextState = remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var selectedSearchFilterItem: SearchFilterItem? by remember { mutableStateOf(null) }
    var selectedFilters by remember {
        mutableStateOf<Map<SearchFilterItem, List<SelectedFilter>>>(emptyMap())
    }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    var isAiSearchMode by remember { mutableStateOf(false) }
    var showBanner by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000)
        showBanner = false
    }

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

            FadeAndSlideAnimatedVisibility(showBanner) {
                NewsBanner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 110.dp),
                    text = "Try our AI assistant, tap on the input field's leading icon!"
                )
            }

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

            when (queryState.value) {
                is SearchUiState.Success -> {
                    SearchSuccessView(
                        result = (queryState.value as SearchUiState.Success).search,
                        onCocktailClick = onCocktailClick
                    )
                }

                is SearchUiState.Error -> {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        text = "Query Error"
                    )
                }

                else -> {}
            }

            when (filterState.value) {
                is FilterUiState.Success -> {
                    // todo: add here filter success view
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        text = "Filter Success"
                    )
                }

                is FilterUiState.Error -> {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        color = Color.White,
                        text = "Filter Error"
                    )
                }

                else -> {}
            }
        }

        // we put it here since it must be out of a scrollable component
        if (queryState.value is SearchUiState.Loading || filterState.value is FilterUiState.Loading) {
            CocktailLoadingAnimation(
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 150.dp)
            )
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
private fun FadeAndSlideAnimatedVisibility(
    condition: Boolean,
    content: @Composable (AnimatedVisibilityScope.() -> Unit)
) = AnimatedVisibility(
    visible = condition,
    enter = fadeIn() + slideInVertically(),
    exit = fadeOut() + slideOutVertically(),
    content = content
)

@Composable
private fun SearchSuccessView(
    result: SearchModel,
    onCocktailClick: (String) -> Unit
) {
    result.results.forEach { drink ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                with(drink as SearchItem.Cocktail) {
                    MainDrinkCard(
                        modifier = Modifier
                            .padding(6.dp),
                        id = id,
                        name = name,
                        category = category,
                        imageString = imageUrl,
                        isFavorite = favorite,
                        userName = username,
                        onClick = onCocktailClick
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreenContent {}
}