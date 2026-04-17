package com.openclassrooms.rebonnte.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val loginState by viewModel.loginState.collectAsState()
    val user by viewModel.user.collectAsState()
    val credentialManager = CredentialManager.create(context)

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
        onLoginClick = {
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_google_client_id))
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            scope.launch {
                try {
                    val result = credentialManager.getCredential(
                        context = context,
                        request = request
                    )
                    val idToken = result.credential.data.getString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN")
                    if (idToken != null) {
                        viewModel.onSignInResult(idToken)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    )
}

@Composable
private fun LoginScreenContent(
    loginState: LoginState,
    user: User?,
    onLoginClick: () -> Unit,
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
            Text(text = "Welcome back, ${user.name}", style = MaterialTheme.typography.headlineMedium)
            Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(text = "Welcome to Rebonnte", style = MaterialTheme.typography.headlineMedium)
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        if (loginState is LoginState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = onLoginClick) {
                Text(text = "Sign in with Google")
            }
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
            onLoginClick = {}
        )
    }
}
