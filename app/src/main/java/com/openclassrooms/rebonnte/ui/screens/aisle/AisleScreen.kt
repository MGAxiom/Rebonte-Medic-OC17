package com.openclassrooms.rebonnte.ui.screens.aisle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.ui.components.RebonnteItem
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun AisleScreen(
    viewModel: AisleViewModel,
    onAisleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val aisles by viewModel.aisles.collectAsState()

    AisleScreenContent(
        aisles = aisles,
        onAisleClick = onAisleClick,
        modifier = modifier
    )
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
