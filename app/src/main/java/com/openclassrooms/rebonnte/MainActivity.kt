package com.openclassrooms.rebonnte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import com.openclassrooms.rebonnte.ui.ThemeViewModel
import com.openclassrooms.rebonnte.ui.screens.login.LoginScreen
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.screens.main.MainScreen
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = koinViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsStateWithLifecycle()
            
            RebonnteTheme(darkTheme = isDarkTheme ?: androidx.compose.foundation.isSystemInDarkTheme()) {
                RebonnteAppContent()
            }
        }
    }
}

@Composable
fun RebonnteAppContent() {
    val loginViewModel: LoginViewModel = koinViewModel()
    val currentUser by loginViewModel.user.collectAsStateWithLifecycle()

    if (currentUser == null) {
        LoginScreen(
            viewModel = loginViewModel,
            onLoginSuccess = {
                // User is logged in, currentUser state will update automatically
            }
        )
    } else {
        MainScreen()
    }
}
