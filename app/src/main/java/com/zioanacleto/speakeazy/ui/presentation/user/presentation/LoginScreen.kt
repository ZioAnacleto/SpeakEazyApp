package com.zioanacleto.speakeazy.ui.presentation.user.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271

// TODO: not in use right now
@Composable
fun LoginComponent(
    modifier: Modifier = Modifier
) {
    var mailTextState by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var passwordTextState by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(41, 20, 51, 255),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp),
                value = mailTextState,
                onValueChange = { mail ->
                    mailTextState = mail
                },
                label = {
                    Text(
                        color = Color.White,
                        text = "Email"
                    )
                },
                placeholder = {
                    Text(
                        color = Color.White,
                        text = "Insert your email"
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email,
                    showKeyboardOnFocus = true
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YellowFFE271,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = YellowFFE271
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp),
                value = passwordTextState,
                onValueChange = { mail ->
                    passwordTextState = mail
                },
                label = {
                    Text(
                        color = Color.White,
                        text = "Passcode"
                    )
                },
                placeholder = {
                    Text(
                        color = Color.White,
                        text = "Insert your 5 digits passcode"
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.NumberPassword,
                    showKeyboardOnFocus = true
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YellowFFE271,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = YellowFFE271
                ),
                visualTransformation = PasswordVisualTransformation()
            )
        }
    }
}

@Preview
@Composable
fun LoginComponentPreview() {
    LoginComponent()
}