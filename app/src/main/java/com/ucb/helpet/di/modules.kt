package com.ucb.helpet.di

import com.ucb.helpet.features.login.data.api.FirebaseService
import com.ucb.helpet.features.login.data.database.AppDatabase
import com.ucb.helpet.features.login.data.datasource.LoginRemoteDataSource
import com.ucb.helpet.features.login.data.repository.LoginRepositoryImpl
import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.features.login.domain.usecase.RegisterUserUseCase
import com.ucb.helpet.features.login.domain.usecases.ForgotPasswordUseCase
import com.ucb.helpet.features.login.domain.usecases.IsUserLoggedInUseCase
import com.ucb.helpet.features.login.domain.usecases.LoginUseCase
import com.ucb.helpet.features.login.domain.usecases.LogoutUseCase
import com.ucb.helpet.features.login.presentation.LoginViewModel
import com.ucb.helpet.features.login.presentation.forgotpassword.ForgotPasswordViewModel
import com.ucb.helpet.features.login.presentation.register.RegisterViewModel
import com.ucb.helpet.features.profile.presentation.ProfileViewModel
import com.ucb.helpet.features.splash.presentation.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Firebase
    single { FirebaseService() }

    // Database (Room for local session)
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().authTokenDao() }

    // RemoteDataSource
    single { LoginRemoteDataSource(get()) } // This will now get FirebaseService

    // Repositories
    single<LoginRepository> { LoginRepositoryImpl(get(), get()) }

    // UseCases
    factory { RegisterUserUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) } // Will be refactored later for Firebase
    factory { LoginUseCase(get()) }
    factory { IsUserLoggedInUseCase(get()) }
    factory { LogoutUseCase(get()) }

    // ViewModels
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) } // Will be refactored later for Firebase
    viewModel { SplashViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
