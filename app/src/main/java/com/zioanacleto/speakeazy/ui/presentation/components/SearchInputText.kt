package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271
import kotlinx.coroutines.launch

@Composable
fun SearchInputText(
    modifier: Modifier = Modifier,
    queryTextState: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onTextFieldFocused: (Boolean) -> Unit,
    onLeadingIconClick: (Boolean) -> Unit,
    onButtonSearchClick: (Boolean, String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isTextFieldFocused by remember { mutableStateOf(false) }
    var isAiSearchMode by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .onFocusChanged {
                isTextFieldFocused = it.isFocused
                onTextFieldFocused(isTextFieldFocused)
            },
        value = queryTextState,
        onValueChange = onQueryChange,
        label = {
            Text(
                color = Color.White,
                text = if (isAiSearchMode) "Search with AI" else "Search"
            )
        },
        placeholder = {
            Text(
                color = Color.White,
                text = if (isAiSearchMode) "Ask to our AI assistant" else "Search cocktails"
            )
        },
        leadingIcon = {
            SearchToggleIcon {
                isAiSearchMode = it
                onLeadingIconClick(isAiSearchMode)
            }
        },
        trailingIcon = {
            if (isTextFieldFocused) {
                // Icon to clear search text
                Icon(
                    modifier = Modifier
                        .clickable {
                            onQueryChange(TextFieldValue(""))
                        },
                    painter = rememberVectorPainter(Icons.Outlined.Clear),
                    contentDescription = "Clear query",
                    tint = Color.White
                )
            } else {
                // todo: camera cocktail recognition
            }
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
                focusManager.clearFocus()
                onButtonSearchClick(isAiSearchMode, queryTextState.text)
            }
        ),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = YellowFFE271,
            unfocusedBorderColor = Color.DarkGray,
            cursorColor = YellowFFE271
        )
    )
}

@Composable
private fun SearchToggleIcon(
    onIconClick: (Boolean) -> Unit
) {
    var currentIconIsAi by remember { mutableStateOf(false) }

    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(1f) }

    val scope = rememberCoroutineScope()

    Icon(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
            }
            .clickable {
                scope.launch {
                    // Current icon bigger
                    scale.animateTo(
                        1.2f,
                        animationSpec = tween(150)
                    )

                    // Fade out
                    alpha.animateTo(
                        0.5f,
                        animationSpec = tween(60)
                    )

                    // Change icon while is still invisible
                    currentIconIsAi = !currentIconIsAi
                    onIconClick(currentIconIsAi)

                    // Fade in new icon (still bigger)
                    alpha.animateTo(
                        1f,
                        animationSpec = tween(60)
                    )

                    // Current icon normal sized
                    scale.animateTo(
                        1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
            },
        painter = rememberVectorPainter(
            if (currentIconIsAi)
                Icons.Default.AccountCircle
            else
                Icons.Default.Search
        ),
        contentDescription = null,
        tint = Color.White
    )
}

@Preview
@Composable
private fun SearchInputTextPreview() {
    SearchInputText(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        queryTextState = TextFieldValue(""),
        onQueryChange = {},
        onLeadingIconClick = {},
        onTextFieldFocused = {}
    ) { _, _ -> }
}