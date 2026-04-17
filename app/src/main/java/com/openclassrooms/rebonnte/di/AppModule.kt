package com.openclassrooms.rebonnte.di

import com.openclassrooms.rebonnte.data.repository.AisleRepositoryImpl
import com.openclassrooms.rebonnte.data.repository.AuthRepositoryImpl
import com.openclassrooms.rebonnte.data.repository.MedicineRepositoryImpl
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.repository.AuthRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.ui.screens.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.screens.medicine.MedicineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AisleRepository> { AisleRepositoryImpl() }
    single<MedicineRepository> { MedicineRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl() }

    viewModel { AisleViewModel(get()) }
    viewModel { MedicineViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}
