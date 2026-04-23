package com.openclassrooms.rebonnte.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun ProfileScreen(
    viewModel: LoginViewModel
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.profileLoading.collectAsState()
    val error by viewModel.profileError.collectAsState()
    var name by remember(user?.name) { mutableStateOf(user?.name ?: "") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearProfileError()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { viewModel.uploadProfilePicture(it) }
        }
    )

    ProfileScreenContent(
        name = name,
        onNameChange = { name = it },
        onSaveName = { viewModel.updateDisplayName(name) },
        initialName = user?.name ?: "",
        email = user?.email ?: "",
        photoUrl = user?.photoUrl,
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        onSignOutClick = { viewModel.signOut() },
        onAddImageClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )
}

@Composable
private fun ProfileScreenContent(
    name: String,
    onNameChange: (String) -> Unit,
    onSaveName: () -> Unit,
    initialName: String,
    email: String,
    photoUrl: String?,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onSignOutClick: () -> Unit,
    onAddImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        val isNameChanged = name != initialName && name.isNotBlank()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .alpha(if (isLoading) 0.5f else 1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserProfilePicture(
                    imageUrl = photoUrl,
                    onAddImageClick = if (isLoading) ({}) else onAddImageClick
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text(stringResource(R.string.name_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text(stringResource(R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = onSaveName,
                modifier = Modifier.padding(top = 20.dp),
                enabled = !isLoading && isNameChanged
            ) {
                Text("Save changes")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSignOutClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                Text(text = stringResource(R.string.sign_out))
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun UserProfilePicture(
    imageUrl: String?,
    onAddImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                shape = CircleShape,
                brush = Brush.sweepGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
            .clickable { onAddImageClick() },
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = rememberVectorPainter(Icons.Default.Person),
                error = rememberVectorPainter(Icons.Default.Person),
                contentDescription = stringResource(R.string.profile_picture_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_image_description),
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    RebonnteTheme {
        ProfileScreenContent(
            name = "John Doe",
            onNameChange = {},
            onSaveName = {},
            initialName = "John Doe",
            email = "john.doe@example.com",
            photoUrl = null,
            isLoading = false,
            snackbarHostState = remember { SnackbarHostState() },
            onSignOutClick = {},
            onAddImageClick = {}
        )
    }
}
