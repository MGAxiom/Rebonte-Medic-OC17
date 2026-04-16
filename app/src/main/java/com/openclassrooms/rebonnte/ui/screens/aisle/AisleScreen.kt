package com.openclassrooms.rebonnte.ui.screens.aisle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.openclassrooms.rebonnte.ui.components.RebonnteItem

@Composable
fun AisleScreen(
    viewModel: AisleViewModel,
    onAisleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val aisles by viewModel.aisles.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(aisles, key = { it.name }) { aisle ->
            RebonnteItem(
                title = aisle.name,
                onClick = { onAisleClick(aisle.name) },
                modifier = modifier
            )
        }
    }
}
