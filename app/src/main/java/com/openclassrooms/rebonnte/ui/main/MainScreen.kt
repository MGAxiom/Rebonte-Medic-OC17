package com.openclassrooms.rebonnte.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.navigation.Screens
import com.openclassrooms.rebonnte.ui.screens.aisle.AisleDetailScreen
import com.openclassrooms.rebonnte.ui.screens.aisle.AisleScreen
import com.openclassrooms.rebonnte.ui.screens.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.screens.medicine.MedicineAddScreen
import com.openclassrooms.rebonnte.ui.screens.medicine.MedicineDetailScreen
import com.openclassrooms.rebonnte.ui.screens.medicine.MedicineScreen
import com.openclassrooms.rebonnte.ui.screens.medicine.MedicineViewModel
import com.openclassrooms.rebonnte.ui.screens.profile.ProfileScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    aisleViewModel: AisleViewModel = koinViewModel(),
    medicineViewModel: MedicineViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // We manually extract the current destination object to maintain compatibility with existing components
    val currentDestination: Any? = remember(navBackStackEntry) {
        val destination = navBackStackEntry?.destination
        when {
            destination?.hasRoute<Screens.Aisle>() == true -> Screens.Aisle
            destination?.hasRoute<Screens.Medicine>() == true -> Screens.Medicine
            destination?.hasRoute<Screens.AisleDetail>() == true -> navBackStackEntry?.toRoute<Screens.AisleDetail>()
            destination?.hasRoute<Screens.MedicineDetail>() == true -> navBackStackEntry?.toRoute<Screens.MedicineDetail>()
            destination?.hasRoute<Screens.MedicineAdd>() == true -> Screens.MedicineAdd
            destination?.hasRoute<Screens.Profile>() == true -> Screens.Profile
            else -> null
        }
    }

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
                    navController.navigate(Screens.Aisle) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToMedicine = {
                    navController.navigate(Screens.Medicine) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Screens.Profile) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        floatingActionButton = {
            MainFloatingActionButton(
                currentDestination = currentDestination,
                aislesNotEmpty = aisles.isNotEmpty(),
                onAddRandomAisle = { aisleViewModel.addRandomAisle() },
                onNavigateToAddMedicine = { navController.navigate(Screens.MedicineAdd) },
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
        NavHost(
            navController = navController,
            startDestination = Screens.Aisle,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screens.Aisle> {
                AisleScreen(
                    viewModel = koinViewModel(),
                    onAisleClick = { name ->
                        navController.navigate(Screens.AisleDetail(name))
                    }
                )
            }
            composable<Screens.Medicine> {
                MedicineScreen(
                    viewModel = koinViewModel(),
                    onDetailClick = { name ->
                        navController.navigate(Screens.MedicineDetail(name))
                    }
                )
            }
            composable<Screens.AisleDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<Screens.AisleDetail>()
                AisleDetailScreen(
                    name = route.name,
                    viewModel = koinViewModel(),
                    onMedicineClick = { medicineName ->
                        navController.navigate(Screens.MedicineDetail(medicineName))
                    }
                )
            }
            composable<Screens.MedicineDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<Screens.MedicineDetail>()
                MedicineDetailScreen(
                    name = route.name,
                    viewModel = koinViewModel()
                )
            }
            composable<Screens.MedicineAdd> {
                MedicineAddScreen(
                    viewModel = medicineViewModel,
                    aislesList = aisles,
                    onMedicineCreated = {
                        navController.popBackStack()
                    }
                )
            }
            composable<Screens.Profile> {
                ProfileScreen(viewModel = loginViewModel)
            }
        }
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
