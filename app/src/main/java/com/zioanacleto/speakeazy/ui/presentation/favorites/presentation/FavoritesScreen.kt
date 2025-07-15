package com.zioanacleto.speakeazy.ui.presentation.favorites.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.zioanacleto.speakeazy.ui.presentation.main.domain.MainFilterItem
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.DrinkModel
import org.koin.androidx.compose.getViewModel

@Composable
fun FavoritesScreen(
    onCocktailClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 14.dp, end = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        val viewModel: FavoritesViewModel = getViewModel()

        when (val state = viewModel.favoritesUiState.collectAsState(FavoritesUiState.Loading).value) {
            is FavoritesUiState.Success -> {
                FavoritesScreenSuccessView(
                    favorites = state.favorites.favorites,
                    onCocktailClick = onCocktailClick
                )
            }

            is FavoritesUiState.Error -> {
                FavoritesScreenErrorView(
                    errorMessage = state.exception?.message ?: "Generic Error"
                )
            }

            is FavoritesUiState.Loading -> {
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
private fun FavoritesScreenSuccessView(
    favorites: List<DrinkModel>,
    onCocktailClick: (String) -> Unit
) {
    var currentStringSize by remember { mutableFloatStateOf(44f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newComponentSize = currentStringSize + (delta * 0.05f)
                val previousComponentSize = currentStringSize

                currentStringSize = newComponentSize.coerceIn(28f, 44f)
                val consumedSize = currentStringSize - previousComponentSize

                return Offset(x = 0f, y = consumedSize)
            }
        }
    }

    Column(
        modifier = Modifier.nestedScroll(nestedScrollConnection)
    ) {
        Text(
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, bottom = 16.dp),
            text = "My favorites",
            color = Color.White,
            fontSize = TextUnit(currentStringSize, TextUnitType.Sp),
            fontWeight = FontWeight.SemiBold
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                MainFilterView(
                    filterList = MainFilterItem.entries
                )
            }
            items(favorites, key = { it.id }) {
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

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
        }
    }
}

@Composable
fun FavoritesScreenErrorView(
    errorMessage: String
) {
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