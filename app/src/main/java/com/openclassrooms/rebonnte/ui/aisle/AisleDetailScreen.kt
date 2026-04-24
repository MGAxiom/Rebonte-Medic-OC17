package com.openclassrooms.rebonnte.ui.aisle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openclassrooms.rebonnte.ui.components.SwipeableItem
import com.openclassrooms.rebonnte.ui.state.MedicineUiState
import com.openclassrooms.rebonnte.ui.medicine.MedicineViewModel

@Composable
fun AisleDetailScreen(
    name: String,
    viewModel: MedicineViewModel,
    modifier: Modifier = Modifier,
    onMedicineClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is MedicineUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is MedicineUiState.Success -> {
                val filteredMedicines = state.medicines.filter { it.nameAisle == name }
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredMedicines, key = { it.name }) { medicine ->
                        SwipeableItem(
                            title = medicine.name,
                            subtitle = "Stock: ${medicine.stock}",
                            onDelete = { viewModel.removeMedicine(medicine.name) },
                            onClick = { onMedicineClick(medicine.name) },
                            modifier = Modifier
                        )
                    }
                }
            }
            is MedicineUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
