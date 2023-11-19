package hu.tb.minichefy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import hu.tb.minichefy.presentation.navigation.MainNavigation
import hu.tb.minichefy.presentation.ui.theme.MiniChefyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniChefyTheme {
                MainNavigation()
            }
        }
    }
}