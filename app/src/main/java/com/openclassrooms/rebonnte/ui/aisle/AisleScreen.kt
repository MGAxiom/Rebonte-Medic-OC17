package com.openclassrooms.rebonnte.ui.aisle

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.openclassrooms.rebonnte.ui.components.RebonnteItem

@Composable
fun AisleScreen(viewModel: AisleViewModel) {
    val aisles by viewModel.aisles.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(aisles) { aisle ->
            RebonnteItem(
                title = aisle.name,
                onClick = {
                    startDetailActivity(context, aisle.name)
                }
            )
        }
    }
}

private fun startDetailActivity(context: Context, name: String) {
    val intent = Intent(context, AisleDetailActivity::class.java).apply {
        putExtra("nameAisle", name)
    }
    context.startActivity(intent)
}
