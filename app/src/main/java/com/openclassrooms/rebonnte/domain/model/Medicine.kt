package com.openclassrooms.rebonnte.domain.model

data class Medicine(
    val id: String = "",
    val name: String = "",
    val stock: Int = 0,
    val nameAisle: String = "",
    val histories: List<History> = emptyList()
)
