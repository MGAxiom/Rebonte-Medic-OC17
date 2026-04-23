package com.openclassrooms.rebonnte.ui.aisle

import androidx.lifecycle.ViewModel
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.usecase.AddAisleUseCase
import com.openclassrooms.rebonnte.domain.usecase.GetAislesUseCase
import kotlinx.coroutines.flow.StateFlow

class AisleViewModel(
    private val getAislesUseCase: GetAislesUseCase,
    private val addAisleUseCase: AddAisleUseCase
) : ViewModel() {
    val aisles: StateFlow<List<Aisle>> = getAislesUseCase()

    fun addRandomAisle() {
        addAisleUseCase.addRandomAisle()
    }
}