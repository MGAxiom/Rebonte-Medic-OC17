package com.openclassrooms.rebonnte.di

import com.openclassrooms.rebonnte.data.repository.AisleRepositoryImpl
import com.openclassrooms.rebonnte.data.repository.AuthRepositoryImpl
import com.openclassrooms.rebonnte.data.repository.MedicineRepositoryImpl
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import com.openclassrooms.rebonnte.domain.repository.AuthRepository
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.data.repository.UserRepositoryImpl
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import com.openclassrooms.rebonnte.domain.usecase.UploadProfilePictureUseCase
import com.openclassrooms.rebonnte.ui.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.medicine.MedicineViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::AisleRepositoryImpl) { bind<AisleRepository>() }
    singleOf(::MedicineRepositoryImpl) { bind<MedicineRepository>() }
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    factoryOf(::UploadProfilePictureUseCase)

    viewModelOf(::AisleViewModel)
    viewModelOf(::MedicineViewModel)
    viewModelOf(::LoginViewModel)
}
