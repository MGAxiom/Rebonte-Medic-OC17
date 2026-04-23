package com.openclassrooms.rebonnte.ui.medicine

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.openclassrooms.rebonnte.ui.components.SwipeableItem

@Composable
fun MedicineScreen(
    viewModel: MedicineViewModel,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val medicines by viewModel.medicines.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(medicines, key = { it.name }) { medicine ->
            SwipeableItem(
                title = medicine.name,
                subtitle = "Stock: ${medicine.stock}",
                onDelete = { viewModel.removeMedicine(medicine.name) },
                onClick = { onDetailClick(medicine.name) },
                modifier = modifier
            )
        }
    }
}
