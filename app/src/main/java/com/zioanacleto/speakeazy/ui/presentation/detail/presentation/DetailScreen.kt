package com.zioanacleto.speakeazy.ui.presentation.detail.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.core.domain.main.model.DrinkModel
import com.zioanacleto.speakeazy.core.domain.main.model.IngredientModel
import com.zioanacleto.speakeazy.core.domain.main.model.InstructionModel
import com.zioanacleto.speakeazy.ui.presentation.components.BackFloatingButton
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailDetailInformation
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.GradientCircularShadowBox
import com.zioanacleto.speakeazy.ui.presentation.components.IngredientView
import com.zioanacleto.speakeazy.ui.presentation.components.NewsBanner
import com.zioanacleto.speakeazy.ui.presentation.components.VideoPlayer
import com.zioanacleto.speakeazy.ui.presentation.components.bottomSheetStyle
import com.zioanacleto.speakeazy.ui.presentation.components.parallaxLayoutModifier
import com.zioanacleto.speakeazy.ui.presentation.components.speakEazyGradientBackground
import com.zioanacleto.speakeazy.ui.presentation.components.withAlpha
import com.zioanacleto.speakeazy.ui.theme.BottomBarBackground
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    cocktailId: String,
    onInstructionsClick: (String, String, List<InstructionModel>) -> Unit,
    onBackButtonClick: () -> Unit
) {
    DetailScreenContent(modifier, cocktailId, onInstructionsClick, onBackButtonClick)
}

@Composable
private fun DetailScreenContent(
    modifier: Modifier = Modifier,
    cocktailId: String,
    onInstructionsClick: (String, String, List<InstructionModel>) -> Unit,
    onBackButton: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val fadeIn by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(600, easing = LinearOutSlowInEasing),
        label = "fadeInAnimation"
    )

    val viewModel: DetailViewModel = koinViewModel()

    // do this because we don't have a base view to do the call in
    val uiStateFlow = remember(cocktailId) { viewModel.detailUiState(cocktailId) }
    val detailUiState = uiStateFlow.collectAsStateWithLifecycle(DetailUiState.Loading)

    when (detailUiState.value) {
        is DetailUiState.Success -> {
            DetailScreenSuccessView(
                cocktail = (detailUiState.value as DetailUiState.Success).detail.drinks.first(),
                startAnimation = startAnimation,
                fadeIn = fadeIn,
                onBackButton = onBackButton,
                onInstructionsClick = onInstructionsClick,
                onAddFavoriteClick = viewModel::setFavoriteCocktail,
                onDeleteFavoriteClick = viewModel::deleteFavoriteCocktail
            )

            val cocktail = (detailUiState.value as DetailUiState.Success).detail.drinks.first()

            LaunchedEffect(cocktailId) {
                startAnimation = true
                viewModel.updateVisualizations(cocktail.id)
            }
        }

        is DetailUiState.Error -> {
            DetailScreenErrorView(
                errorMessage = (detailUiState.value as? DetailUiState.Error)
                    ?.exception?.message ?: "Generic Error"
            )
        }

        is DetailUiState.Loading -> {
            CocktailLoadingAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun DetailScreenSuccessView(
    cocktail: DrinkModel,
    startAnimation: Boolean,
    fadeIn: Float,
    onBackButton: () -> Unit,
    onInstructionsClick: (String, String, List<InstructionModel>) -> Unit,
    onAddFavoriteClick: (String, String) -> Unit,
    onDeleteFavoriteClick: (String) -> Unit
) {
    val isFavorite = remember { mutableStateOf(cocktail.favorite) }
    val scrollState = rememberScrollState()
    var titleY by remember { mutableFloatStateOf(0f) }
    var showCollapsedToolbar by remember { mutableStateOf(false) }
    val toolbarHeightPx = with(LocalDensity.current) { 60.dp.toPx() }

    LaunchedEffect(scrollState.value, titleY) {
        showCollapsedToolbar = titleY <= toolbarHeightPx
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .graphicsLayer(alpha = fadeIn)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // if video is present show video view (without parallax), otherwise image view
            if (cocktail.videoUrl.isEmpty())
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .parallaxLayoutModifier(scrollState, 2),
                    contentDescription = "Cocktail Detail",
                    model = cocktail.imageUrl,
                    contentScale = ContentScale.FillWidth
                )
            else
                VideoPlayer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp),
                    videoUrl = cocktail.videoUrl
                )

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .bottomSheetStyle()
                        .speakEazyGradientBackground(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 26.dp, start = 16.dp)
                            .onGloballyPositioned { coords ->
                                titleY = coords.positionInWindow().y
                            },
                        fontSize = TextUnit(32f, TextUnitType.Sp),
                        color = Color.White,
                        text = cocktail.name
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 16.dp),
                        fontSize = TextUnit(18f, TextUnitType.Sp),
                        color = Color.White,
                        text = cocktail.category
                    )

                    CocktailDetailInformation(
                        modifier = Modifier
                            .padding(top = 40.dp, bottom = 40.dp)
                            .heightIn(40.dp, 60.dp),
                        glassType = cocktail.glass,
                        cocktailType = cocktail.type,
                        method = cocktail.method
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            color = Color.White,
                            text = stringResource(R.string.detail_screen__ingredients_section_title),
                            fontSize = TextUnit(22f, TextUnitType.Sp)
                        )
                        Text(
                            color = Color.White,
                            text = stringResource(
                                R.string.detail_screen__ingredients_section_subtitle,
                                cocktail.ingredients.size
                            ),
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    }

                    cocktail.ingredients.forEach { ingredient ->
                        IngredientView(ingredient)
                    }

                    NewsBanner(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.detail_screen__banner_text)
                    )

                    // todo: think about something else
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 4.dp)
                            .clickable {
                                onInstructionsClick(
                                    cocktail.name,
                                    cocktail.glass,
                                    cocktail.instructions
                                )
                            }
                            .border(2.dp, YellowFFE271, RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        text = stringResource(R.string.detail_screen__directions_button),
                        color = YellowFFE271,
                        fontSize = TextUnit(18f, TextUnitType.Sp)
                    )

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        AnimatedVisibility(
            visible = !showCollapsedToolbar,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DetailScreenTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                onBackButton = onBackButton,
                isFavorite = isFavorite.value,
                onFavoriteClicked = {
                    if (it)
                        onDeleteFavoriteClick(cocktail.id)
                    else
                        onAddFavoriteClick(cocktail.id, cocktail.name)

                    isFavorite.value = !isFavorite.value
                }
            )
        }

        AnimatedVisibility(
            visible = showCollapsedToolbar,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DetailScreenCollapsedTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                title = cocktail.name,
                onBackButton = onBackButton,
                isFavorite = isFavorite.value,
                onFavoriteClicked = {
                    if (it)
                        onDeleteFavoriteClick(cocktail.id)
                    else
                        onAddFavoriteClick(cocktail.id, cocktail.name)

                    isFavorite.value = !isFavorite.value
                }
            )
        }
    }
}

@Composable
private fun DetailScreenErrorView(errorMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .speakEazyGradientBackground(),
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

@Composable
private fun DetailScreenTopBar(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClicked: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // glowing shadow
    val initialHaloBorderWidth = 0.dp
    val pressedHaloBorderWidth = 48.dp

    val animatedSpread by animateDpAsState(
        targetValue = if (isPressed) pressedHaloBorderWidth else initialHaloBorderWidth,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // buttons
        BackFloatingButton(
            modifier = Modifier,
            onBackButton = onBackButton
        )

        GradientCircularShadowBox(
            modifier = Modifier
                .padding(end = 16.dp, top = 40.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Black.withAlpha()),
            color = Color.White,
            initialHaloBorderWidth = initialHaloBorderWidth,
            pressedHaloBorderWidth = pressedHaloBorderWidth,
            interactionSource = interactionSource,
            onClick = {}
        ) {
            val icon =
                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        onFavoriteClicked(isFavorite)
                    },
                painter = rememberVectorPainter(icon),
                contentDescription = "Favorite Button",
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
private fun DetailScreenCollapsedTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackButton: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClicked: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
            .background(BottomBarBackground), // todo: do you like it?
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // buttons
        Icon(
            modifier = Modifier
                .padding(start = 16.dp, top = 45.dp)
                .clickable { onBackButton() },
            painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ArrowBack),
            contentDescription = "Back Button",
            tint = Color.White
        )

        Text(
            modifier = Modifier
                .align(Alignment.Bottom)
                .padding(bottom = 10.dp),
            text = title,
            color = Color.White,
            fontSize = TextUnit(16f, TextUnitType.Sp)
        )

        val icon =
            if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
        Image(
            modifier = Modifier
                .padding(end = 16.dp, top = 45.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onFavoriteClicked(isFavorite)
                },
            painter = rememberVectorPainter(icon),
            contentDescription = "Favorite Button",
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@Composable
@Preview
fun DetailScreen() {
    DetailScreenSuccessView(
        cocktail = DrinkModel(
            id = "1",
            name = "Test Cocktail",
            category = "Test Category",
            imageUrl = "",
            instructions = listOf(
                InstructionModel(
                    type = "test",
                    instruction = "Test Instructions"
                )
            ),
            glass = "Test Glass",
            type = "Test Type",
            method = "Test Method",
            ingredients = listOf(
                IngredientModel(
                    id = "1",
                    name = "Test Ingredient",
                    imageUrl = "",
                    measureCl = "1cl",
                    measureOz = "1oz"
                ),
                IngredientModel(
                    id = "2",
                    name = "Test Ingredient 2",
                    imageUrl = "",
                    measureCl = "2cl",
                    measureOz = "2oz"
                )
            )
        ),
        startAnimation = true,
        fadeIn = 1f,
        onBackButton = {},
        onInstructionsClick = { _, _, _ -> },
        onAddFavoriteClick = { _, _ -> },
        onDeleteFavoriteClick = {}
    )
}

@Composable
@Preview
fun DetailError() {
    DetailScreenErrorView("Exception")
}