package com.zioanacleto.speakeazy.ui.presentation.main.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.presentation.components.BannerSection
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.MainDrinkCard
import com.zioanacleto.speakeazy.ui.presentation.main.domain.model.BannerModel
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
            .padding(top = 50.dp)
    ) {
        when (val state = viewModel.homeUiState.collectAsState(HomeUiState.Loading).value) {
            is HomeUiState.Success -> {
                MainScreenHomeSuccessView(
                    sections = state.home.sections,
                    banner = state.home.banner,
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
    banner: BannerModel? = null,
    onCocktailClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(top = 10.dp)
    ) {
        // dividing sections in two lists
        val halfIndex = banner?.position?.toInt() ?: (sections.size / 2)
        val (firstList, secondList) =
            sections.take(halfIndex) to sections.subList(halfIndex, sections.size)

        // section content layout
        val content: @Composable LazyItemScope.(HomeSectionModel) -> Unit = { section ->
            Text(
                modifier = Modifier
                    .padding(top = 10.dp, start = 24.dp, end = 24.dp),
                text = section.name,
                color = Color.White,
                fontSize = TextUnit(24f, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold
            )

            LazyRow(
                modifier = Modifier
                    .padding(start = 14.dp, end = 14.dp)
            ) {
                items(section.cocktails, key = { it.id }) { drink ->
                    with(drink) {
                        MainDrinkCard(
                            modifier = Modifier
                                .padding(6.dp)
                                .animateItem(),
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
            }
        }

        // main Home title
        item {
            Text(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 16.dp),
                text = "Choose your cocktail",
                color = Color.White,
                fontSize = TextUnit(36f, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold,
                lineHeight = TextUnit(42f, TextUnitType.Sp)
            )
        }

        // first section half
        items(firstList, key = { it.id }) { section ->
            content(section)
        }

        // banner
        banner?.let { bannerModel ->
            item {
                BannerSection(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .height(200.dp),
                    banner = bannerModel
                ) { onCocktailClick(it) }
            }
        }

        // second section half
        items(secondList, key = { it.id }) { section ->
            content(section)
        }

        // last spacer to avoid overlapping with menu
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