package com.zioanacleto.speakeazy.ui.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.window.DialogProperties
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String = "",
    confirmButtonText: String = "Ok",
    dismissButtonText: String = "Cancel",
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        backgroundColor = Color(41, 20, 51, 200),
        onDismissRequest = onDismissButtonClick,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmButtonClick()
                    onDismissButtonClick()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = YellowFFE271,
                    backgroundColor = Color.Transparent
                )
            ) {
                Text(
                    text = confirmButtonText
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissButtonClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = YellowFFE271,
                    backgroundColor = Color.Transparent
                )
            ) {
                Text(
                    text = dismissButtonText
                )
            }
        },
        title = {
            Text(
                text = title,
                color = Color.White,
                fontSize = TextUnit(22f, TextUnitType.Sp),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = message,
                color = Color.White,
                fontSize = TextUnit(16f, TextUnitType.Sp)
            )
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        title = "Test",
        message = "Questo è un test per vedere cosa succede",
        onConfirmButtonClick = { },
        onDismissButtonClick = { }
    )
}