package com.openclassrooms.rebonnte.domain.model

data class Medicine(
    val name: String,
    val stock: Int,
    val nameAisle: String,
    val histories: List<History>
)
