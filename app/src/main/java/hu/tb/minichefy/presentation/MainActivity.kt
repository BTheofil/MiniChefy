package hu.tb.minichefy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.tb.minichefy.presentation.navigation.DrawerNavigation
import hu.tb.minichefy.presentation.navigation.MainNavigation
import hu.tb.minichefy.presentation.ui.LocalModalDrawerState
import hu.tb.minichefy.presentation.ui.theme.MiniChefyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            MiniChefyTheme {
                val navController = rememberNavController()
                val drawerState = LocalModalDrawerState.current

                DrawerNavigation(
                    drawerState = drawerState,
                    navController = navController,
                ) {
                    Scaffold { padding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .consumeWindowInsets(padding)
                        ) {
                            MainNavigation(navController)
                        }
                    }
                }

            }
        }
    }
}