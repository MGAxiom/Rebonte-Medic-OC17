package com.openclassrooms.rebonnte.domain.model

import java.util.UUID

data class Medicine(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val stock: Int,
    val nameAisle: String,
    val histories: List<History>
)
