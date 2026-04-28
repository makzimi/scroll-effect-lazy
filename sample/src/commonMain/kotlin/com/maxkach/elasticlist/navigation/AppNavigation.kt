package com.maxkach.elasticlist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maxkach.elasticlist.ui.effects.EffectPreset
import com.maxkach.elasticlist.ui.screens.coffee.CoffeeScreen
import com.maxkach.elasticlist.ui.screens.ecommerce.EcommerceScreen
import com.maxkach.elasticlist.ui.screens.home.HomeScreen
import com.maxkach.elasticlist.ui.screens.menu.MenuScreen
import com.maxkach.elasticlist.ui.screens.social.SocialScreen

object Destinations {
    const val HOME = "home"
    const val ECOMMERCE = "ecommerce"
    const val MENU = "menu"
    const val SOCIAL = "social"
    const val COFFEE = "coffee"
}

@Composable
fun AppNavigation() {
    var effectPreset by rememberSaveable { mutableStateOf(EffectPreset.None) }
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destinations.HOME) {
        composable(Destinations.HOME) {
            HomeScreen(
                onEcommerceClick = { navController.navigate(Destinations.ECOMMERCE) },
                onMenuClick = { navController.navigate(Destinations.MENU) },
                onSocialClick = { navController.navigate(Destinations.SOCIAL) },
                onCoffeeClick = { navController.navigate(Destinations.COFFEE) },
            )
        }
        composable(Destinations.ECOMMERCE) {
            EcommerceScreen(
                effectPreset = effectPreset,
                onEffectPreset = { effectPreset = it },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Destinations.MENU) {
            MenuScreen(
                effectPreset = effectPreset,
                onEffectPreset = { effectPreset = it },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Destinations.SOCIAL) {
            SocialScreen(
                effectPreset = effectPreset,
                onEffectPreset = { effectPreset = it },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Destinations.COFFEE) {
            CoffeeScreen(
                effectPreset = effectPreset,
                onEffectPreset = { effectPreset = it },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
