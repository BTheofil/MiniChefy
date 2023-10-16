package hu.tb.minichefy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import hu.tb.minichefy.navigation.MainNavigation
import hu.tb.minichefy.ui.theme.MiniChefyTheme

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