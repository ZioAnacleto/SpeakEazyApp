package com.zioanacleto.speakeazy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zioanacleto.buffa.compose.coloredEdgeToEdge
import com.zioanacleto.buffa.logging.AnacletoLogger
import com.zioanacleto.speakeazy.core.analytics.network.NetworkMonitor
import com.zioanacleto.speakeazy.navigation.SpeakEazyNavHost
import com.zioanacleto.speakeazy.navigation.TopBarDestination
import com.zioanacleto.speakeazy.ui.presentation.components.BottomBar
import com.zioanacleto.speakeazy.ui.presentation.components.CreateCocktailTopBar
import com.zioanacleto.speakeazy.ui.presentation.components.MainTopBar
import com.zioanacleto.speakeazy.ui.presentation.components.OfflineBlockingScreen
import com.zioanacleto.speakeazy.ui.presentation.components.speakEazyGradientBackground
import com.zioanacleto.speakeazy.ui.presentation.components.withAlpha
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.UserUiState
import com.zioanacleto.speakeazy.ui.theme.LocalSnackBarHostState
import com.zioanacleto.speakeazy.ui.theme.SpeakEazyTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()
    private val networkMonitor: NetworkMonitor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var userState: UserUiState = UserUiState.Loading

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userUiState
                    .map { it }
                    .distinctUntilChanged()
                    .collect { isUserLogged ->
                        userState = isUserLogged
                    }
            }
        }

        splashScreen.setKeepOnScreenCondition {
            AnacletoLogger.mumbling(
                "User state for splash screen: ${viewModel.userUiState.value}"
            )
            viewModel.userUiState.value is UserUiState.Loading
        }

        coloredEdgeToEdge()

        setContent {
            val appState = rememberSpeakEazyAppState(
                isUserLogged = userState is UserUiState.Success,
                networkMonitor = networkMonitor
            )
            val showTopBar = appState.currentBottomBarDestination != null
            val showBottomBar = appState.showBottomBar

            val snackbarHostState = remember { SnackbarHostState() }

            SpeakEazyTheme {
                CompositionLocalProvider(
                    values = arrayOf(
                        LocalSnackBarHostState provides snackbarHostState
                    )
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState) {
                                Snackbar(
                                    snackbarData = it,
                                    backgroundColor = Color.White.withAlpha(0.85f),
                                    contentColor = Color.Black
                                )
                            }
                        },
                        topBar = {
                            // avoid passing appState to components, we pass variables or lambdas
                            AnimatedVisibility(
                                visible = showTopBar,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                if (showBottomBar) {
                                    MainTopBar(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 30.dp),
                                    ) { appState.navigateToTopBarDestination(TopBarDestination.USER_SETTINGS) }
                                } else {
                                    CreateCocktailTopBar(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 30.dp)
                                    ) { appState.navController.popBackStack() }
                                }
                            }
                        },
                        bottomBar = {
                            with(appState) {
                                AnimatedVisibility(
                                    visible = showBottomBar,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    BottomBar(
                                        modifier = Modifier
                                            .padding(horizontal = 50.dp)
                                            .windowInsetsPadding(WindowInsets.navigationBars),
                                        isUserLogged = isUserLogged,
                                        animateCreateButton = animateCreateButton,
                                        currentDestination = currentDestination
                                    ) { navigateToBottomBarDestination(it) }
                                }
                            }
                        }
                    ) { _ -> mainContentOrOfflineView(appState) }
                }
            }
        }
    }

    @Composable
    private fun mainContentOrOfflineView(appState: SpeakEazyAppState) = with(appState) {
        // observe the network status
        val isOffline by isOffline.collectAsStateWithLifecycle()

        // show blocking screen if offline
        if (isOffline) {
            OfflineBlockingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .speakEazyGradientBackground()
            )
        } else {
            SpeakEazyNavHost(
                appState = this,
                modifier = Modifier
                    .speakEazyGradientBackground()
            )
        }
    }
}