package com.openclassrooms.rebonnte.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess()
        } else if (loginState is LoginState.Error) {
            Toast.makeText(context, (loginState as LoginState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    LoginScreenContent(
        loginState = loginState,
        user = user,
        onGoogleLoginClick = { viewModel.signInWithGoogle(context) }
    )
}

@Composable
private fun LoginScreenContent(
    loginState: LoginState,
    user: User?,
    onGoogleLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (user != null) {
            Text(
                text = "Welcome back, ${user.name}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(text = "Welcome to Rebonnte", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (loginState is LoginState.Loading) {
            CircularProgressIndicator()
        } else {
            LoginScreenButtons(
                onGoogleLoginClick = onGoogleLoginClick,
                onEmailLoginClick = {}
            )
        }
    }
}

@Composable
private fun LoginScreenButtons(
    onGoogleLoginClick: () -> Unit,
    onEmailLoginClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Button(
            onClick = onGoogleLoginClick,
            modifier = Modifier.width(BUTTON_WIDTH.dp)
        ) {
            Text(text = "Sign in with Google")
        }
        Button(
            onClick = onEmailLoginClick,
            modifier = Modifier.width(BUTTON_WIDTH.dp)
        ) {
            Text(text = "Sign in with credentials")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    RebonnteTheme {
        LoginScreenContent(
            loginState = LoginState.Idle,
            user = null,
            onGoogleLoginClick = {}
        )
    }
}

private const val BUTTON_WIDTH = 200