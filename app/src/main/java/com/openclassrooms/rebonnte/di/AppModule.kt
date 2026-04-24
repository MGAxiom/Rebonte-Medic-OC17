package com.openclassrooms.rebonnte.di

import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.rebonnte.data.repository.AisleRepositoryImpl
import com.openclassrooms.rebonnte.data.repository.AuthRepositoryImpl
import com.openclassrooms.rebonnte.data.repository.MedicineRepositoryImpl
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.repository.AuthRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.data.repository.UserRepositoryImpl
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.usecase.UploadProfilePictureUseCase
import com.openclassrooms.rebonnte.domain.usecase.AddAisleUseCase
import com.openclassrooms.rebonnte.domain.usecase.GetAislesUseCase
import com.openclassrooms.rebonnte.domain.usecase.AddMedicineUseCase
import com.openclassrooms.rebonnte.domain.usecase.GetMedicinesUseCase
import com.openclassrooms.rebonnte.domain.usecase.UpdateMedicineStockUseCase
import com.openclassrooms.rebonnte.domain.usecase.RemoveMedicineUseCase
import com.openclassrooms.rebonnte.ui.screens.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.screens.medicine.MedicineViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { FirebaseFirestore.getInstance() }
    
    single<AisleRepository> { AisleRepositoryImpl(androidContext(), get()) }
    single<MedicineRepository> { MedicineRepositoryImpl(androidContext(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(androidContext()) }
    single<UserRepository> { UserRepositoryImpl(androidContext()) }
    
    factoryOf(::UploadProfilePictureUseCase)
    factoryOf(::AddAisleUseCase)
    factoryOf(::GetAislesUseCase)
    factoryOf(::AddMedicineUseCase)
    factoryOf(::GetMedicinesUseCase)
    factoryOf(::UpdateMedicineStockUseCase)
    factoryOf(::RemoveMedicineUseCase)

    viewModelOf(::AisleViewModel)
    viewModelOf(::MedicineViewModel)
    viewModelOf(::LoginViewModel)
}
