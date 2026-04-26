package com.openclassrooms.rebonnte.ui.screens.medicine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.ui.state.MedicineUiState
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme

@Composable
fun MedicineDetailScreen(name: String, viewModel: MedicineViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val medicine = (uiState as? MedicineUiState.Success)?.medicines?.find { it.name == name } ?: return

    MedicineDetailScreenContent(
        medicine = medicine,
        onUpdateStock = { increment ->
            viewModel.updateStock(medicine.name, increment = increment)
        },
        modifier = Modifier,
        state = viewModel.detailListState
    )
}

@Composable
private fun MedicineDetailScreenContent(
    medicine: Medicine,
    onUpdateStock: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    state: androidx.compose.foundation.lazy.LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = state,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = medicine.name,
                onValueChange = {},
                label = { Text("Name") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = medicine.nameAisle,
                onValueChange = {},
                label = { Text("Aisle") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val cdMinusOne = stringResource(R.string.cd_minus_one)
                IconButton(
                    onClick = { onUpdateStock(false) },
                    modifier = Modifier.semantics { contentDescription = cdMinusOne }
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
                TextField(
                    value = medicine.stock.toString(),
                    onValueChange = {},
                    label = { Text("Stock") },
                    enabled = false,
                    modifier = Modifier.weight(1f)
                )
                val cdPlusOne = stringResource(R.string.cd_plus_one)
                IconButton(
                    onClick = { onUpdateStock(true) },
                    modifier = Modifier.semantics { contentDescription = cdPlusOne }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "History", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(medicine.histories, key = { it.id }) { history ->
            HistoryItem(history = history)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HistoryItem(history: History) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = history.medicineName, fontWeight = FontWeight.Bold)
            Text(text = "User: ${history.userId}")
            Text(text = "Date: ${history.date}")
            Text(text = "Details: ${history.details}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineDetailScreenPreview() {
    RebonnteTheme {
        MedicineDetailScreenContent(
            medicine = Medicine(
                name = "Paracetamol",
                nameAisle = "Aisle 1",
                stock = 10,
                histories = listOf(
                    History(medicineName = "Paracetamol", userId = "user1", date = "2023-10-27", details = "Stock incremented")
                )
            ),
            onUpdateStock = {}
        )
    }
}
