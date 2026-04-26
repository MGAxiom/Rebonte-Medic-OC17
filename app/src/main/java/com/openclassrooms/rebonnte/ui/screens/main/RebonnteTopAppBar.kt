package com.openclassrooms.rebonnte.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.components.EmbeddedSearchBar
import com.openclassrooms.rebonnte.ui.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RebonnteTopAppBar(
    currentDestination: Any?,
    searchQuery: String,
    isSearchActive: Boolean,
    onSortNone: () -> Unit,
    onSortName: () -> Unit,
    onSortStock: () -> Unit,
    onQueryChange: (String) -> Unit,
    onActiveChanged: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy((-1).dp)) {
        TopAppBar(
            title = {
                when (currentDestination) {
                    is Screens.Aisle -> Text(text = stringResource(R.string.aisles_direction_name))
                    is Screens.Medicine -> Text(text = stringResource(R.string.medicines_direction_name))
                    is Screens.AisleDetail -> Text(text = currentDestination.name)
                    is Screens.MedicineDetail -> Text(text = currentDestination.name)
                    is Screens.MedicineAdd -> Text(text = stringResource(R.string.add_medicine_direction_name))
                    else -> Text(text = stringResource(R.string.rebonnte_direction_name))
                }
            },
            actions = {
                var expanded by remember { mutableStateOf(false) }

                if (currentDestination == Screens.Medicine) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box {
                            val cdMoreOptions = stringResource(R.string.cd_more_options)
                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier.semantics { contentDescription = cdMoreOptions }
                            ) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                offset = DpOffset(x = 0.dp, y = 0.dp)
                            ) {
                                DropdownMenuItem(
                                    onClick = {
                                        onSortNone()
                                        expanded = false
                                    },
                                    text = { Text(stringResource(R.string.sort_by_none)) }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        onSortName()
                                        expanded = false
                                    },
                                    text = { Text(stringResource(R.string.sort_by_name)) }
                                )
                                DropdownMenuItem(
                                    onClick = {
                                        onSortStock()
                                        expanded = false
                                    },
                                    text = { Text(stringResource(R.string.sort_by_stock)) }
                                )
                            }
                        }
                    }
                }
            }
        )
        if (currentDestination == Screens.Medicine) {
            EmbeddedSearchBar(
                query = searchQuery,
                onQueryChange = onQueryChange,
                isSearchActive = isSearchActive,
                onActiveChanged = onActiveChanged
            )
        }
    }
}
