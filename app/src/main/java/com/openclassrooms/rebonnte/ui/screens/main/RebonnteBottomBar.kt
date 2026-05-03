package com.openclassrooms.rebonnte.ui.screens.main

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
    backStack: List<Any>,
    onNavigateToAisle: () -> Unit,
    onNavigateToMedicine: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val root = backStack.firstOrNull()
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(R.string.aisle_tab)) },
            selected = root == Screens.Aisle,
            onClick = onNavigateToAisle
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(R.string.medicine_tab)) },
            selected = root == Screens.Medicine,
            onClick = onNavigateToMedicine
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(R.string.account_tab)) },
            selected = root == Screens.Profile,
            onClick = onNavigateToProfile
        )
    }
}
