package com.openclassrooms.rebonnte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.openclassrooms.rebonnte.ui.aisle.AisleDetailScreen
import com.openclassrooms.rebonnte.ui.aisle.AisleScreen
import com.openclassrooms.rebonnte.ui.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.components.EmbeddedSearchBar
import com.openclassrooms.rebonnte.ui.login.LoginScreen
import com.openclassrooms.rebonnte.ui.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.medicine.MedicineAddScreen
import com.openclassrooms.rebonnte.ui.medicine.MedicineDetailScreen
import com.openclassrooms.rebonnte.ui.medicine.MedicineScreen
import com.openclassrooms.rebonnte.ui.medicine.MedicineViewModel
import com.openclassrooms.rebonnte.ui.navigation.Screens
import com.openclassrooms.rebonnte.ui.theme.RebonnteTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RebonnteTheme {
                val loginViewModel = koinViewModel<LoginViewModel>()
                val currentUser by loginViewModel.currentUser.collectAsState()

                val backStack = remember { mutableStateListOf<Any>(Screens.Aisle) }
                val currentDestination = backStack.lastOrNull()
                val aisleViewModel = koinViewModel<AisleViewModel>()
                val medicineViewModel = koinViewModel<MedicineViewModel>()
                val aisles by aisleViewModel.aisles.collectAsState()

                val scope = rememberCoroutineScope()

                if (currentUser == null) {
                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = {
                            // User is logged in, currentUser state will update automatically
                        }
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            var isSearchActive by rememberSaveable { mutableStateOf(false) }
                            var searchQuery by remember { mutableStateOf("") }

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

                                        IconButton(onClick = { loginViewModel.signOut() }) {
                                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sign Out")
                                        }

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
                                    selected = currentDestination == Screens.Aisle || currentDestination is Screens.AisleDetail,
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
                                    selected = currentDestination == Screens.Medicine || currentDestination is Screens.MedicineDetail || currentDestination == Screens.MedicineAdd,
                                    onClick = {
                                        if (currentDestination != Screens.Medicine) {
                                            backStack.clear()
                                            backStack.add(Screens.Medicine)
                                        }
                                    }
                                )
                            }
                        },
                        floatingActionButton = {
                            if (currentDestination == Screens.Aisle) {
                                FloatingActionButton(onClick = {
                                    aisleViewModel.addRandomAisle()
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_aisle_content_description))
                                }
                            } else if (currentDestination == Screens.Medicine && aisles.isNotEmpty()) {
                                FloatingActionButton(onClick = {
                                    backStack.add(Screens.MedicineAdd)
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_medicine_content_description))
                                }
                            } else if (currentDestination is Screens.MedicineDetail) {
                                val showButton by remember {
                                    derivedStateOf {
                                        medicineViewModel.detailListState.firstVisibleItemIndex > 0 ||
                                                medicineViewModel.detailListState.firstVisibleItemScrollOffset > 0
                                    }
                                }
                                AnimatedVisibility(
                                    visible = showButton,
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    FloatingActionButton(onClick = {
                                        scope.launch {
                                            medicineViewModel.detailListState.animateScrollToItem(0)
                                        }
                                    }) {
                                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Back to top")
                                    }
                                }
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
                                            onAisleClick = { name ->
                                                backStack.add(Screens.AisleDetail(name))
                                            }
                                        )
                                    }

                                    is Screens.Medicine -> NavEntry(key) {
                                        MedicineScreen(
                                            viewModel = koinViewModel(),
                                            onDetailClick = { name ->
                                                backStack.add(Screens.MedicineDetail(name))
                                            }
                                        )
                                    }

                                    is Screens.AisleDetail -> NavEntry(key) {
                                        AisleDetailScreen(
                                            name = key.name,
                                            viewModel = koinViewModel(),
                                            onMedicineClick = { medicineName ->
                                                backStack.add(Screens.MedicineDetail(medicineName))
                                            }
                                        )
                                    }

                                    is Screens.MedicineDetail -> NavEntry(key) {
                                        MedicineDetailScreen(
                                            name = key.name,
                                            viewModel = koinViewModel()
                                        )
                                    }
                                    is Screens.MedicineAdd -> NavEntry(key) {
                                        MedicineAddScreen(
                                            viewModel = medicineViewModel,
                                            aislesList = aisles,
                                            onMedicineCreated = {
                                                backStack.removeLastOrNull()
                                            }
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
}
