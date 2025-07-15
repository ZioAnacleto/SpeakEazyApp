package com.zioanacleto.speakeazy.ui.presentation.main.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.MainDrinkCard
import com.zioanacleto.speakeazy.ui.presentation.components.MainFilterView
import com.zioanacleto.speakeazy.ui.presentation.components.speakEazyGradientBackground
import com.zioanacleto.speakeazy.ui.presentation.main.domain.MainFilterItem
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.HomeSectionModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onCocktailClick: (String) -> Unit
) {
    MainScreenContent(modifier, onCocktailClick)
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    onCocktailClick: (String) -> Unit
) {
    val viewModel: MainViewModel = getViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 14.dp, end = 14.dp)
    ) {
        when(val state = viewModel.homeUiState.collectAsState(HomeUiState.Loading).value) {
            is HomeUiState.Success -> {
                MainScreenHomeSuccessView(
                    sections = state.home.sections,
                    onCocktailClick = onCocktailClick
                )
            }
            is HomeUiState.Error -> {
                MainScreenErrorView(
                    errorMessage = state.exception?.message ?: "Generic Error"
                )
            }

            is HomeUiState.Loading -> {
                CocktailLoadingAnimation(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp)
                )
            }
        }
    }
}

@Composable
fun MainScreenHomeSuccessView(
    modifier: Modifier = Modifier,
    sections: List<HomeSectionModel>,
    onCocktailClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(top = 10.dp)
    ) {
        item {
            Text(
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp, bottom = 16.dp),
                text = "Choose your cocktail",
                color = Color.White,
                fontSize = TextUnit(36f, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold,
                lineHeight = TextUnit(42f, TextUnitType.Sp)
            )
        }
        items(sections, key = { it.id }) { section ->
            Text(
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp),
                text = section.name,
                color = Color.White,
                fontSize = TextUnit(24f, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold
            )

            LazyRow {
                items(section.cocktails, key = { it.id }) {
                    MainDrinkCard(
                        modifier = Modifier
                            .padding(6.dp)
                            .animateItem(),
                        id = it.id,
                        name = it.name,
                        category = it.category,
                        imageString = it.imageUrl,
                        isFavorite = it.favorite,
                        onClick = onCocktailClick
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }
}

@Composable
private fun MainScreenErrorView(errorMessage: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sorry, there was a problem...",
            color = Color.White,
            fontSize = TextUnit(24f, TextUnitType.Sp)
        )
        Text(
            modifier = Modifier
                .padding(top = 10.dp),
            text = errorMessage,
            color = Color.White,
            fontSize = TextUnit(14f, TextUnitType.Sp)
        )
    }
}