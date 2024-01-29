package hu.tb.minichefy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.tb.minichefy.presentation.navigation.MainNavigation
import hu.tb.minichefy.presentation.screens.recipe.navigation.RECIPE_GRAPH
import hu.tb.minichefy.presentation.screens.storage.navigation.STORAGE_GRAPH
import hu.tb.minichefy.presentation.ui.theme.MiniChefyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniChefyTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.Filled.Favorite, contentDescription = "asd") },
                                label = { Text("asd") },
                                selected = true,
                                onClick = {
                                    navController.navigate(STORAGE_GRAPH)
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Filled.Favorite, contentDescription = "asd") },
                                label = { Text("qwe") },
                                selected = true,
                                onClick = {
                                    navController.navigate(RECIPE_GRAPH)
                                }
                            )
                        }
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .consumeWindowInsets(padding)
                    ) {
                        MainNavigation(
                            navController
                        )
                    }
                }
            }
        }
    }
}