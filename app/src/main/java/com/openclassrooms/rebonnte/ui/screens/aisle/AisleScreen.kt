package com.openclassrooms.rebonnte.ui.screens.aisle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.ui.components.RebonnteItem
import com.openclassrooms.rebonnte.ui.state.AisleUiState
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

import androidx.compose.material3.CircularProgressIndicator

@Composable
fun AisleScreen(
    viewModel: AisleViewModel,
    onAisleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val error by viewModel.error.collectAsState(null)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (val state = uiState) {
            is AisleUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is AisleUiState.Success -> {
                AisleScreenContent(
                    aisles = state.aisles,
                    onAisleClick = onAisleClick
                )
            }

            is AisleUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun AisleScreenContent(
    aisles: List<Aisle>,
    onAisleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(aisles, key = { it.name }) { aisle ->
            RebonnteItem(
                title = aisle.name,
                onClick = { onAisleClick(aisle.name) },
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AisleScreenPreview() {
    RebonnteTheme {
        AisleScreenContent(
            aisles = listOf(
                Aisle("Aisle 1"),
                Aisle("Aisle 2"),
                Aisle("Aisle 3")
            ),
            onAisleClick = {}
        )
    }
}
