package com.openclassrooms.rebonnte.ui.aisle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.openclassrooms.rebonnte.ui.components.RebonnteItem
import com.openclassrooms.rebonnte.ui.components.SwipeableItem
import com.openclassrooms.rebonnte.ui.medicine.MedicineViewModel

@Composable
fun AisleDetailScreen(
    name: String,
    viewModel: MedicineViewModel,
    modifier: Modifier = Modifier,
    onMedicineClick: (String) -> Unit
) {
    val medicines by viewModel.medicines.collectAsStateWithLifecycle()
    val filteredMedicines = medicines.filter { it.nameAisle == name }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(filteredMedicines, key = { it.name }) { medicine ->
            SwipeableItem(
                title = medicine.name,
                subtitle = "Stock: ${medicine.stock}",
                onDelete = { viewModel.removeMedicine(medicine.name) },
                onClick = { onMedicineClick(medicine.name) },
                modifier = modifier
            )
        }
    }
}
