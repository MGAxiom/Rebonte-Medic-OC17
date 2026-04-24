package com.openclassrooms.rebonnte.ui.aisle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.ui.components.RebonnteItem

@Composable
fun AisleScreen(
    viewModel: AisleViewModel,
    onAisleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val aislesResult by viewModel.aisles.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        aislesResult.fold(
            onSuccess = { aisles ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(aisles, key = { it.name }) { aisle ->
                        RebonnteItem(
                            title = aisle.name,
                            onClick = { onAisleClick(aisle.name) },
                            modifier = Modifier
                        )
                    }
                }
            },
            onFailure = { error ->
                Text(
                    text = error.message ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        )
    }
}
