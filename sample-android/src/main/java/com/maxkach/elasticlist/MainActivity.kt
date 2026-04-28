package com.maxkach.elasticlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maxkach.scrolleffects.navigation.AppNavigation
import com.maxkach.elasticlist.ui.theme.ElasticListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElasticListTheme {
                AppNavigation()
            }
        }
    }
}
