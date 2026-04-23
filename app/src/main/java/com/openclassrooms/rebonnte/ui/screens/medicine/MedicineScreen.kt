package com.openclassrooms.rebonnte.ui.screens.medicine

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.ui.components.SwipeableItem
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun MedicineScreen(
    viewModel: MedicineViewModel,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val medicines by viewModel.medicines.collectAsState()

    MedicineScreenContent(
        medicines = medicines,
        onDetailClick = onDetailClick,
        onDeleteMedicine = { viewModel.removeMedicine(it) },
        modifier = modifier
    )
}

@Composable
private fun MedicineScreenContent(
    medicines: List<Medicine>,
    onDetailClick: (String) -> Unit,
    onDeleteMedicine: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(medicines, key = { it.name }) { medicine ->
            SwipeableItem(
                title = medicine.name,
                subtitle = "Stock: ${medicine.stock}",
                onDelete = { onDeleteMedicine(medicine.name) },
                onClick = { onDetailClick(medicine.name) },
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineScreenPreview() {
    RebonnteTheme {
        MedicineScreenContent(
            medicines = listOf(
                Medicine(name = "Paracetamol", stock = 10, nameAisle = "Aisle 1"),
                Medicine(name = "Ibuprofen", stock = 5, nameAisle = "Aisle 2")
            ),
            onDetailClick = {},
            onDeleteMedicine = {}
        )
    }
}
