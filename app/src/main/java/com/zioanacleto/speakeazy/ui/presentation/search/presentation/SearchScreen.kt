package com.zioanacleto.speakeazy.ui.presentation.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PlatformImeOptions
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.ExpandableHorizontalFilterView
import com.zioanacleto.speakeazy.ui.presentation.components.SelectedFilter
import com.zioanacleto.speakeazy.ui.presentation.search.domain.SearchFilterItem
import com.zioanacleto.speakeazy.ui.presentation.search.domain.model.SearchLandingModel
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    SearchScreenContent(modifier)
}

@Composable
private fun SearchScreenContent(
    modifier: Modifier = Modifier
) {
    val viewModel: SearchViewModel = getViewModel()

    val landingState = remember { viewModel.landingUiState }

    when (val uiState = landingState.collectAsStateWithLifecycle().value) {
        is SearchLandingUiState.Success -> {
            SearchScreenWithFilter(
                viewModel = viewModel,
                data = uiState.data,
                onButtonSearchClick = {
                    viewModel.search(it)
                }
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
    viewModel: SearchViewModel,
    data: SearchLandingModel,
    onButtonSearchClick: (String) -> Unit
) {
    val filterState = viewModel.filterUiState.collectAsStateWithLifecycle()
    val queryState = viewModel.queryUiState.collectAsStateWithLifecycle()
    val queryTextState = remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var selectedSearchFilterItem: SearchFilterItem? by remember { mutableStateOf(null) }
    var selectedFilters by remember { mutableStateOf<List<SelectedFilter>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .hideKeyboardOnTouch()
            .padding(top = 60.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 20.dp, bottom = 16.dp),
            text = "Search cocktails",
            color = Color.White,
            fontSize = TextUnit(36f, TextUnitType.Sp),
            fontWeight = FontWeight.SemiBold,
            lineHeight = TextUnit(42f, TextUnitType.Sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            value = queryTextState.value,
            onValueChange = { query ->
                queryTextState.value = query
            },
            label = {
                Text(
                    color = Color.White,
                    text = "Search"
                )
            },
            placeholder = {
                Text(
                    color = Color.White,
                    text = "Ask to our AI assistant"
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = TextUnit(14f, TextUnitType.Sp)
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onButtonSearchClick(queryTextState.value.text)
                }
            ),
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowFFE271,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = YellowFFE271
            )
        )

        ExpandableHorizontalFilterView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            list = SearchFilterItem.entries.toList(),
            onFilterSelectClick = { searchFilterItem ->
                val filters = if (searchFilterItem == selectedSearchFilterItem)
                    selectedFilters
                else when (searchFilterItem) {
                    SearchFilterItem.INGREDIENTS -> {
                        data.ingredients.map {
                            SelectedFilter(it.name, false)
                        }
                    }

                    SearchFilterItem.TAGS -> {
                        data.tags.map {
                            SelectedFilter(it.name, false)
                        }
                    }

                    else -> listOf()
                }

                selectedSearchFilterItem = searchFilterItem
                filters
            },
            onFilterDoneClick = { list ->
                if (selectedFilters != list) {
                    selectedFilters = list
                    selectedSearchFilterItem?.let {
                        viewModel.filter(it, selectedFilters)
                    }
                }
            }
        )

        when (queryState.value) {
            is SearchUiState.Success -> {
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    color = Color.White,
                    text = "Query Success"
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

            is SearchUiState.Loading -> {
                CocktailLoadingAnimation(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            else -> {}
        }

        when (filterState.value) {
            is FilterUiState.Success -> {
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

            is FilterUiState.Loading -> {
                CocktailLoadingAnimation(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            else -> {}
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreenContent()
}