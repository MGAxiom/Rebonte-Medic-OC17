package com.openclassrooms.rebonnte.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.aisle.AisleDetailScreen
import com.openclassrooms.rebonnte.ui.aisle.AisleScreen
import com.openclassrooms.rebonnte.ui.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.medicine.MedicineAddScreen
import com.openclassrooms.rebonnte.ui.medicine.MedicineDetailScreen
import com.openclassrooms.rebonnte.ui.medicine.MedicineScreen
import com.openclassrooms.rebonnte.ui.medicine.MedicineViewModel
import com.openclassrooms.rebonnte.ui.navigation.Screens
import com.openclassrooms.rebonnte.ui.screens.profile.ProfileScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    aisleViewModel: AisleViewModel = koinViewModel(),
    medicineViewModel: MedicineViewModel = koinViewModel()
) {
    val backStack = remember { mutableStateListOf<Any>(Screens.Aisle) }
    val currentDestination = backStack.lastOrNull()
    val aisles by aisleViewModel.aisles.collectAsState()
    val scope = rememberCoroutineScope()

    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RebonnteTopAppBar(
                currentDestination = currentDestination,
                searchQuery = searchQuery,
                isSearchActive = isSearchActive,
                onSignOut = { loginViewModel.signOut() },
                onSortNone = { medicineViewModel.sortByNone() },
                onSortName = { medicineViewModel.sortByName() },
                onSortStock = { medicineViewModel.sortByStock() },
                onQueryChange = {
                    medicineViewModel.filterByName(it)
                    searchQuery = it
                },
                onActiveChanged = { isSearchActive = it }
            )
        },
        bottomBar = {
            RebonnteBottomBar(
                currentDestination = currentDestination,
                onNavigateToAisle = {
                    if (currentDestination != Screens.Aisle) {
                        backStack.clear()
                        backStack.add(Screens.Aisle)
                    }
                },
                onNavigateToMedicine = {
                    if (currentDestination != Screens.Medicine) {
                        backStack.clear()
                        backStack.add(Screens.Medicine)
                    }
                },
                onNavigateToProfile = {
                    if (currentDestination != Screens.Profile) {
                        backStack.clear()
                        backStack.add(Screens.Profile)
                    }
                }
            )
        },
        floatingActionButton = {
            MainFloatingActionButton(
                currentDestination = currentDestination,
                aislesNotEmpty = aisles.isNotEmpty(),
                onAddRandomAisle = { aisleViewModel.addRandomAisle() },
                onNavigateToAddMedicine = { backStack.add(Screens.MedicineAdd) },
                onScrollToTop = {
                    scope.launch {
                        medicineViewModel.detailListState.animateScrollToItem(0)
                    }
                },
                showScrollToTop = remember {
                    derivedStateOf {
                        medicineViewModel.detailListState.firstVisibleItemIndex > 0 ||
                                medicineViewModel.detailListState.firstVisibleItemScrollOffset > 0
                    }
                }.value
            )
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
                    is Screens.Profile -> NavEntry(key) {
                        ProfileScreen(
                            viewModel = loginViewModel
                        )
                    }
                    else -> NavEntry(Unit) {}
                }
            }
        )
    }
}

@Composable
fun MainFloatingActionButton(
    currentDestination: Any?,
    aislesNotEmpty: Boolean,
    onAddRandomAisle: () -> Unit,
    onNavigateToAddMedicine: () -> Unit,
    onScrollToTop: () -> Unit,
    showScrollToTop: Boolean
) {
    if (currentDestination == Screens.Aisle) {
        FloatingActionButton(onClick = onAddRandomAisle) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_aisle_content_description))
        }
    } else if (currentDestination == Screens.Medicine && aislesNotEmpty) {
        FloatingActionButton(onClick = onNavigateToAddMedicine) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_medicine_content_description))
        }
    } else if (currentDestination is Screens.MedicineDetail) {
        AnimatedVisibility(
            visible = showScrollToTop,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(onClick = onScrollToTop) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = stringResource(R.string.back_to_top))
            }
        }
    }
}
