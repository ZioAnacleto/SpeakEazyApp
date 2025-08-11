package com.zioanacleto.speakeazy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zioanacleto.buffa.compose.coloredEdgeToEdge
import com.zioanacleto.speakeazy.navigation.SpeakEazyNavHost
import com.zioanacleto.speakeazy.ui.presentation.components.BottomBar
import com.zioanacleto.speakeazy.ui.presentation.components.TopBar
import com.zioanacleto.speakeazy.ui.presentation.components.speakEazyGradientBackground
import com.zioanacleto.speakeazy.ui.theme.LocalSnackBarHostState
import com.zioanacleto.speakeazy.ui.theme.SpeakEazyTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coloredEdgeToEdge()

        setContent {
            val appState = rememberSpeakEazyAppState()
            val showTopBar = appState.currentBottomBarDestination != null

            val snackbarHostState = remember { SnackbarHostState() }

            SpeakEazyTheme {
                CompositionLocalProvider(
                    values = arrayOf(
                        LocalSnackBarHostState provides snackbarHostState
                    )
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        },
                        topBar = {
                            AnimatedVisibility(
                                visible = showTopBar,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                TopBar(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 30.dp),
                                    appState = appState
                                )
                            }
                        },
                        bottomBar = {
                            BottomBar(
                                modifier = Modifier
                                    .padding(horizontal = 50.dp)
                                    .windowInsetsPadding(WindowInsets.navigationBars),
                                appState = appState
                            )
                        }
                    ) { _ ->
                        SpeakEazyNavHost(
                            appState = appState,
                            modifier = Modifier
                                .speakEazyGradientBackground()
                        )
                    }
                }
            }
        }
    }
}