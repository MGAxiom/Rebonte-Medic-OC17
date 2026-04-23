package com.openclassrooms.rebonnte.ui.aisle

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.openclassrooms.rebonnte.ui.components.RebonnteItem
import com.openclassrooms.rebonnte.ui.components.SwipeableItem

@Composable
fun AisleScreen(
    viewModel: AisleViewModel,
    onAisleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val aisles by viewModel.aisles.collectAsStateWithLifecycle()

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
