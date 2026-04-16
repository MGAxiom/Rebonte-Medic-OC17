package com.openclassrooms.rebonnte.di

import com.openclassrooms.rebonnte.data.repository.AisleRepositoryImpl
import com.openclassrooms.rebonnte.data.repository.AuthRepositoryImpl
import com.openclassrooms.rebonnte.data.repository.MedicineRepositoryImpl
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.repository.AuthRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.ui.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.medicine.MedicineViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { AisleRepositoryImpl() } bind AisleRepository::class
    single { MedicineRepositoryImpl() } bind MedicineRepository::class
    single { AuthRepositoryImpl() } bind AuthRepository::class

    viewModelOf(::AisleViewModel)
    viewModelOf(::MedicineViewModel)
    viewModelOf(::LoginViewModel)
}
