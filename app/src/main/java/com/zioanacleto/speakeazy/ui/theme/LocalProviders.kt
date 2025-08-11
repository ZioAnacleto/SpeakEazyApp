package com.zioanacleto.speakeazy.ui.theme

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

/**
 *  Local instance provided via CompositionLocalProvider of [SnackbarHostState]
 */
val LocalSnackBarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}