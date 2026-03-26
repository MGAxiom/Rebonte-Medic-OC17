package com.openclassrooms.rebonnte.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screens: NavKey {
    @Serializable data object Login : Screens
    @Serializable data object Aisle : Screens
    @Serializable data object Medicine : Screens
    @Serializable data object Profile : Screens
}