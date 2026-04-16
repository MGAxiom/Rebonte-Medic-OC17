package com.openclassrooms.rebonnte.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.ui.navigation.Screens

@Composable
fun RebonnteBottomBar(
    currentDestination: Any?,
    onNavigateToAisle: () -> Unit,
    onNavigateToMedicine: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = stringResource(R.string.aisle_tab)
                )
            },
            label = { Text(stringResource(R.string.aisle_tab)) },
            selected = currentDestination == Screens.Aisle ||
                    currentDestination is Screens.AisleDetail,
            onClick = onNavigateToAisle
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(R.string.medicine_tab)
                )
            },
            label = { Text(stringResource(R.string.medicine_tab)) },
            selected = currentDestination == Screens.Medicine ||
                    currentDestination is Screens.MedicineDetail ||
                    currentDestination == Screens.MedicineAdd,
            onClick = onNavigateToMedicine
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = stringResource(R.string.account_tab)
                )
            },
            label = { Text(stringResource(R.string.account_tab)) },
            selected = currentDestination == Screens.Profile,
            onClick = onNavigateToProfile
        )
    }
}
