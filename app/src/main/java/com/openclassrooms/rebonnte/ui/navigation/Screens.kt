package com.openclassrooms.rebonnte.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable data object Login : Screens
    @Serializable data object Aisle : Screens
    @Serializable data object Medicine : Screens
    @Serializable data class MedicineDetail(val name: String) : Screens
    @Serializable data object MedicineAdd : Screens
    @Serializable data class AisleDetail(val name: String) : Screens
    @Serializable data object Profile : Screens
}