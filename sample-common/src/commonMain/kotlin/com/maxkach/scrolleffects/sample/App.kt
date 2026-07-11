package com.maxkach.scrolleffects.sample

import androidx.compose.runtime.Composable
import com.maxkach.scrolleffects.sample.navigation.AppNavigation
import com.maxkach.scrolleffects.sample.ui.theme.SampleTheme

/**
 * Shared root of the sample app. Every platform entry point (Android, iOS,
 * desktop, web) renders this so there's a single source of truth for the UI.
 */
@Composable
fun App() {
    SampleTheme {
        AppNavigation()
    }
}
