package com.openclassrooms.rebonnte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.openclassrooms.rebonnte.ui.screens.login.LoginScreen
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.main.MainScreen
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RebonnteTheme {
                RebonnteAppContent()
            }
        }
    }
}

@Composable
fun RebonnteAppContent() {
    val loginViewModel: LoginViewModel = koinViewModel()
    val currentUser by loginViewModel.user.collectAsState()

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
