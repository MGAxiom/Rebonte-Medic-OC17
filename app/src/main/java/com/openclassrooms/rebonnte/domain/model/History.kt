package com.openclassrooms.rebonnte.domain.model

import java.util.UUID

data class History(
    val id: String = UUID.randomUUID().toString(),
    val medicineName: String,
    val userId: String,
    val date: String,
    val details: String
)
