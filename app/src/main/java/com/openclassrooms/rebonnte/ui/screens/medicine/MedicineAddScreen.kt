package com.openclassrooms.rebonnte.ui.screens.medicine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.Medicine

@Composable
fun MedicineAddScreen(
    viewModel: MedicineViewModel,
    aislesList: List<Aisle>,
    onMedicineCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    MedicineAddScreenContent(
        aislesList = aislesList,
        onCreateMedicineClick = { medicine ->
            viewModel.addMedicine(medicine)
            onMedicineCreated()
        },
        modifier = modifier
    )
}

@Composable
private fun MedicineAddScreenContent(
    aislesList: List<Aisle>,
    onCreateMedicineClick: (Medicine) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var selectedAisle by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    Column(modifier.padding(16.dp)) {
        MedicineAddScreenFields(
            name = name,
            onNameChange = { name = it },
            aislesList = aislesList,
            selectedAisle = selectedAisle,
            onAisleChange = { selectedAisle = it },
            stock = stock,
            onStockChange = { stock = it },
            modifier = Modifier.padding(bottom = 40.dp)
        )
        MedicineConfirmButton(
            onClick = {
                val stockInt = stock.toIntOrNull() ?: 0
                onCreateMedicineClick(
                    Medicine(
                        name = name,
                        nameAisle = selectedAisle,
                        stock = stockInt,
                        histories = emptyList()
                    )
                )
            },
            enabled = name.isNotBlank() && selectedAisle.isNotBlank() && stock.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MedicineAddScreenFields(
    name: String,
    onNameChange: (String) -> Unit,
    aislesList: List<Aisle>,
    selectedAisle: String,
    onAisleChange: (String) -> Unit,
    stock: String,
    onStockChange: (String) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        TextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.medicine_name)) },
            placeholder = {
                Text(stringResource(R.string.medicine_placeholder))
            },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedAisle,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.aisle)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                aislesList.forEach { aisle ->
                    DropdownMenuItem(
                        text = { Text(aisle.name) },
                        onClick = {
                            onAisleChange(aisle.name)
                            expanded = false
                        }
                    )
                }
            }
        }

        TextField(
            value = stock,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onStockChange(newValue)
                }
            },
            label = { Text(stringResource(R.string.stock)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(stringResource(R.string.stock_medicine_placeholder))
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MedicineConfirmButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(stringResource(R.string.medicine_add_confirm_button))
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicineAddScreenPreview() {
    MedicineAddScreenContent(
        aislesList = listOf(
            Aisle("Aisle 1"),
            Aisle("Aisle 2")
        ),
        onCreateMedicineClick = {},
        modifier = Modifier
    )
}