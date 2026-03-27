package com.openclassrooms.rebonnte.ui.aisle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.openclassrooms.rebonnte.ui.components.RebonnteItem
import com.openclassrooms.rebonnte.ui.medicine.MedicineViewModel

@Composable
fun AisleDetailScreen(
    name: String,
    viewModel: MedicineViewModel,
    onMedicineClick: (String) -> Unit
) {
    val medicines by viewModel.medicines.collectAsState()
    val filteredMedicines = medicines.filter { it.nameAisle == name }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(filteredMedicines) { medicine ->
            RebonnteItem(
                title = medicine.name,
                subtitle = "Stock: ${medicine.stock}",
                onClick = { onMedicineClick(medicine.name) }
            )
        }
    }
}
