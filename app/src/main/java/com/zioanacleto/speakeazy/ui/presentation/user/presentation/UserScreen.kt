package com.zioanacleto.speakeazy.ui.presentation.user.presentation

import android.app.Activity
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCancellationBehavior
import com.zioanacleto.buffa.compose.hideKeyboardOnTouch
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.R
import com.zioanacleto.speakeazy.core.domain.user.model.Language
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import com.zioanacleto.speakeazy.ui.presentation.components.BackFloatingButton
import com.zioanacleto.speakeazy.ui.presentation.components.CocktailLoadingAnimation
import com.zioanacleto.speakeazy.ui.presentation.components.CustomSwitchButton
import com.zioanacleto.speakeazy.ui.presentation.components.SafeClickableGenericButton
import com.zioanacleto.speakeazy.ui.theme.LocalSnackBarHostState
import com.zioanacleto.speakeazy.ui.theme.YellowFFE271
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.getViewModel

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit
) {
    UserScreenContent(modifier, onBackButton)
}

@Composable
private fun UserScreenContent(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit
) {
    val viewModel: UserViewModel = getViewModel()
    var emailLink: String? by remember { mutableStateOf(null) }
    var registrationSuccessful by remember { mutableStateOf(false) }
    val snackbarHostState = LocalSnackBarHostState.current
    var snackbarMessage by remember { mutableStateOf("") }

    // retrieving email link from intent
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        emailLink = context.getEmailLink()
        AnacletoLogger.mumbling(
            mumble = "Email link: $emailLink"
        )
    }

    val userUiState: Flow<UserUiState> = remember { viewModel.userUiState }
    when (val state = userUiState.collectAsState(UserUiState.Loading).value) {
        is UserUiState.Success -> {
            if (state.user.name.isNotEmpty()) {
                // show normal view
                AnacletoLogger.mumbling(
                    "User name: ${state.user.name}"
                )

                UserDetailScreen(
                    user = state.user,
                    onBackButton = onBackButton,
                    onLogoutClick = {
                        viewModel.logoutUser()
                        onBackButton()
                    }
                )
            } else {
                // complete user registration asking for name
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn()
                ) {
                    if (registrationSuccessful) {
                        RegisterUserNameScreen(
                            viewModel = viewModel,
                            onBackButton = onBackButton
                        )
                    } else {
                        UserWithoutLinkScreen(
                            onBackButton = onBackButton,
                            onSendAgainButton = {
                                viewModel.sendEmail(state.user.email) {
                                    snackbarMessage =
                                        if (it) "Email successfully sent." else "Email not sent, something went wrong."
                                }
                            }
                        )
                    }
                }

                LaunchedEffect(emailLink) {
                    emailLink?.let {
                        viewModel.finishUserLogin(it, state.user.email) { value ->
                            registrationSuccessful = value
                        }
                    }
                }
            }
        }

        is UserUiState.Error -> {
            RegisterUserScreen(
                viewModel,
                onBackButton
            )
        }

        is UserUiState.Loading -> {
            CocktailLoadingAnimation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                cancellationBehavior = LottieCancellationBehavior.OnIterationFinish
            )
        }
    }

    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage.isNotEmpty())
            snackbarHostState.showSnackbar(snackbarMessage)
    }
}

@Composable
private fun RegisterUserScreen(
    viewModel: UserViewModel,
    onBackButton: () -> Unit
) {
    val snackbarHostState = LocalSnackBarHostState.current
    var snackbarMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .hideKeyboardOnTouch()
    ) {
        BackFloatingButton(
            modifier = Modifier
                .align(Alignment.TopStart),
            onBackButton = onBackButton
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var mailTextState by remember {
                mutableStateOf(
                    TextFieldValue("")
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = "Registrati per entrare nella community dei cocktail più interattiva del panorama.",
                fontSize = TextUnit(36f, TextUnitType.Sp),
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                lineHeight = TextUnit(42f, TextUnitType.Sp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp),
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

            SafeClickableGenericButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp),
                enabled = mailTextState.text.isValidEmail(),
                onClick = {
                    if (mailTextState.text.isNotEmpty()) {
                        viewModel.sendEmail(mailTextState.text) {
                            snackbarMessage =
                                if (it) "Email successfully sent." else "Email not sent, something went wrong."
                        }
                    }
                }
            ) {
                Image(
                    imageVector = Icons.Filled.Email,
                    contentDescription = ""
                )

                Text(
                    text = "Sign up",
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }
        }
    }

    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage.isNotEmpty())
            snackbarHostState.showSnackbar(snackbarMessage)
    }
}

@Composable
private fun RegisterUserNameScreen(
    viewModel: UserViewModel,
    onBackButton: () -> Unit
) {
    var nameTextState by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .hideKeyboardOnTouch()
    ) {
        BackFloatingButton(
            modifier = Modifier
                .align(Alignment.TopStart),
            onBackButton = onBackButton
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = "Quasi fatto, dicci ancora il tuo nome:",
                fontSize = TextUnit(36f, TextUnitType.Sp),
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                lineHeight = TextUnit(42f, TextUnitType.Sp)
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp),
                value = nameTextState,
                onValueChange = { name ->
                    nameTextState = name
                },
                label = {
                    Text(
                        color = Color.White,
                        text = "Name"
                    )
                },
                placeholder = {
                    Text(
                        color = Color.White,
                        text = "Type your name"
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    showKeyboardOnFocus = true
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YellowFFE271,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = YellowFFE271
                )
            )

            SafeClickableGenericButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 24.dp, end = 24.dp),
                enabled = true,
                onClick = {
                    viewModel.updateUserWithName(
                        nameTextState.text.ifEmpty { "Barman anonimo" }
                    )
                    onBackButton()
                }
            ) {
                Image(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = ""
                )

                Text(
                    text = "Done",
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun UserDetailScreen(
    user: UserModel,
    onBackButton: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val viewModel: UserViewModel = getViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        BackFloatingButton(
            modifier = Modifier
                .align(Alignment.Start),
            onBackButton = onBackButton
        )
        FloatingActionButton(
            modifier = Modifier
                .padding(top = 10.dp)
                .size(200.dp)
                .clip(CircleShape),
            onClick = {
                // todo
            }
        ) {
            Image(
                painter = painterResource(R.drawable.default_user_image),
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp),
            text = "Ciao, ${user.getFirstName()}",
            fontSize = TextUnit(32f, TextUnitType.Sp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TextUnit(38f, TextUnitType.Sp)
        )

        Row(
            modifier = Modifier
                .padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Selected language:",
                color = Color.White,
                fontSize = TextUnit(16f, TextUnitType.Sp)
            )

            CustomSwitchButton(
                modifier = Modifier
                    .padding(start = 16.dp),
                buttonHeight = 36.dp,
                firstValue = user.selectedLanguage == Language.ITALIAN,
                onContent = {
                    Text(
                        text = "IT",
                        color = Color.Black
                    )
                },
                offContent = {
                    Text(
                        text = "EN",
                        color = Color.Black
                    )
                }
            ) {
                val currentUser = user.copy(
                    selectedLanguage = if (it) Language.ITALIAN else Language.ENGLISH
                )
                viewModel.updateUserWithLanguage(currentUser)
            }
        }

        SafeClickableGenericButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 24.dp, end = 24.dp),
            enabled = true,
            onClick = onLogoutClick
        ) {
            Image(
                imageVector = Icons.Filled.Delete,
                contentDescription = ""
            )

            Text(
                text = "Logout",
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun UserWithoutLinkScreen(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit,
    onSendAgainButton: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        BackFloatingButton(
            modifier = Modifier
                .align(Alignment.Start),
            onBackButton = onBackButton
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp),
            text = "Abbiamo mandato il link alla mail che ci hai fornito, controlla la tua posta.",
            fontSize = TextUnit(32f, TextUnitType.Sp),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TextUnit(38f, TextUnitType.Sp)
        )

        SafeClickableGenericButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 24.dp, end = 24.dp),
            enabled = true,
            onClick = onSendAgainButton
        ) {
            Image(
                imageVector = Icons.Filled.Email,
                contentDescription = ""
            )

            Text(
                text = "Send again",
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
    }
}

private fun Context.getEmailLink() = (this as? Activity)?.intent?.data?.toString()

private fun String.isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

@Preview
@Composable
private fun RegisterUserScreenPreview() {
    RegisterUserScreen(viewModel = getViewModel()) { }
}

@Preview
@Composable
private fun UserDetailScreenPreview() {
    UserDetailScreen(
        user = UserModel(
            name = "Fabrizio Traversa",
            email = "test@gmail.com"
        ),
        onBackButton = { }
    ) { }
}

@Preview
@Composable
private fun UserWithoutLinkScreenPreview() {
    UserWithoutLinkScreen(
        onBackButton = { },
        onSendAgainButton = {}
    )
}