package com.openclassrooms.rebonnte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.openclassrooms.rebonnte.ui.aisle.AisleScreen
import com.openclassrooms.rebonnte.ui.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.medicine.MedicineScreen
import com.openclassrooms.rebonnte.ui.medicine.MedicineViewModel
import com.openclassrooms.rebonnte.ui.navigation.Screens
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RebonnteTheme {
                val backStack = remember { mutableStateListOf<Any>(Screens.Aisle) }
                val currentDestination = backStack.lastOrNull()
                val aisleViewModel = koinViewModel<AisleViewModel>()
                val medicineViewModel = koinViewModel<MedicineViewModel>()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        var isSearchActive by rememberSaveable { mutableStateOf(false) }
                        var searchQuery by remember { mutableStateOf("") }

                        Column(verticalArrangement = Arrangement.spacedBy((-1).dp)) {
                            TopAppBar(
                                title = { if (currentDestination == Screens.Aisle) Text(text = "Aisle") else Text(text = "Medicines") },
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
                                                IconButton(onClick = { expanded = true }) {
                                                    Icon(Icons.Default.MoreVert, contentDescription = null)
                                                }
                                                DropdownMenu(
                                                    expanded = expanded,
                                                    onDismissRequest = { expanded = false },
                                                    offset = DpOffset(x = 0.dp, y = 0.dp)
                                                ) {
                                                    DropdownMenuItem(
                                                        onClick = {
                                                            medicineViewModel.sortByNone()
                                                            expanded = false
                                                        },
                                                        text = { Text("Sort by None") }
                                                    )
                                                    DropdownMenuItem(
                                                        onClick = {
                                                            medicineViewModel.sortByName()
                                                            expanded = false
                                                        },
                                                        text = { Text("Sort by Name") }
                                                    )
                                                    DropdownMenuItem(
                                                        onClick = {
                                                            medicineViewModel.sortByStock()
                                                            expanded = false
                                                        },
                                                        text = { Text("Sort by Stock") }
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
                                    onQueryChange = {
                                        medicineViewModel.filterByName(it)
                                        searchQuery = it
                                    },
                                    isSearchActive = isSearchActive,
                                    onActiveChanged = { isSearchActive = it }
                                )
                            }
                        }

                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                label = { Text("Aisle") },
                                selected = currentDestination == Screens.Aisle,
                                onClick = { 
                                    if (currentDestination != Screens.Aisle) {
                                        backStack.clear()
                                        backStack.add(Screens.Aisle)
                                    }
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                                label = { Text("Medicine") },
                                selected = currentDestination == Screens.Medicine,
                                onClick = {
                                    if (currentDestination != Screens.Medicine) {
                                        backStack.add(Screens.Medicine)
                                    }
                                }
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            if (currentDestination == Screens.Medicine) {
                                medicineViewModel.addRandomMedicine(aisleViewModel.aisles.value)
                            } else if (currentDestination == Screens.Aisle) {
                                aisleViewModel.addRandomAisle()
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                ) { innerPadding ->
                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { key ->
                            when (key) {
                                is Screens.Aisle -> NavEntry(key) {
                                    AisleScreen(
                                        viewModel = koinViewModel(),
                                    )
                                }

                                is Screens.Medicine -> NavEntry(key) {
                                    MedicineScreen(
                                        viewModel = koinViewModel(),
                                    )
                                }
                                else -> NavEntry(Unit) {}
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmbeddedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable { mutableStateOf(query) }
    val activeChanged: (Boolean) -> Unit = { active ->
        searchQuery = ""
        onQueryChange("")
        onActiveChanged(active)
    }

    val shape: Shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSearchActive) {
            IconButton(onClick = { activeChanged(false) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        BasicTextField(
            value = searchQuery,
            onValueChange = { queryText ->
                searchQuery = queryText
                onQueryChange(queryText)
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        )

        if (isSearchActive && searchQuery.isNotEmpty()) {
            IconButton(onClick = {
                searchQuery = ""
                onQueryChange("")
            }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
