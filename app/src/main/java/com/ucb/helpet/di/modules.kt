package com.ucb.helpet.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.auth.api.identity.Identity
import com.ucb.helpet.features.home.data.datasource.PetRemoteDataSource
import com.ucb.helpet.features.home.data.datasource.StorageDataSource
import com.ucb.helpet.features.home.domain.repository.PetRepository
import com.ucb.helpet.features.home.domain.repository.PetRepositoryImpl
import com.ucb.helpet.features.home.domain.usecase.GetPetByIdUseCase
import com.ucb.helpet.features.home.presentation.detail.PetDetailViewModel
import com.ucb.helpet.features.home.domain.usecase.GetAllPetsUseCase
import com.ucb.helpet.features.home.domain.usecase.GetUserPetsUseCase
import com.ucb.helpet.features.home.domain.usecase.ReportPetUseCase
import com.ucb.helpet.features.home.presentation.HomeViewModel
import com.ucb.helpet.features.home.presentation.report.ReportPetViewModel
import com.ucb.helpet.features.login.data.api.FirebaseService
import com.ucb.helpet.features.login.data.database.AppDatabase
import com.ucb.helpet.features.login.data.datasource.LoginDataStore
import com.ucb.helpet.features.login.data.datasource.LoginRemoteDataSource
import com.ucb.helpet.features.login.data.repository.LoginRepositoryImpl
import com.ucb.helpet.features.login.domain.repository.IRepositoryDataStore
import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.features.login.domain.usecase.RegisterUserUseCase
import com.ucb.helpet.features.login.domain.usecases.ForgotPasswordUseCase
import com.ucb.helpet.features.login.domain.usecases.GetUserProfileUseCase
import com.ucb.helpet.features.login.domain.usecases.IsUserLoggedInUseCase
import com.ucb.helpet.features.login.domain.usecases.LoginUseCase
import com.ucb.helpet.features.login.domain.usecases.LogoutUseCase
import com.ucb.helpet.features.login.presentation.LoginViewModel
import com.ucb.helpet.features.login.presentation.forgotpassword.ForgotPasswordViewModel
import com.ucb.helpet.features.login.presentation.google.GoogleAuthUiClient
import com.ucb.helpet.features.login.presentation.register.RegisterViewModel
import com.ucb.helpet.features.notifications.NotificationHelper
import com.ucb.helpet.features.profile.presentation.ProfileViewModel
import com.ucb.helpet.features.rewards.data.repository.RewardsRepositoryImpl
import com.ucb.helpet.features.rewards.domain.repository.RewardsRepository
import com.ucb.helpet.features.rewards.domain.usecase.GetAvailableRewardsUseCase
import com.ucb.helpet.features.rewards.presentation.RewardsViewModel
import com.ucb.helpet.features.splash.presentation.SplashViewModel
import com.ucb.helpet.features.search.presentation.SearchViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // --- FIREBASE ---
    single { FirebaseService() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseRemoteConfig.getInstance() }

    // --- DATABASE ---
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().authTokenDao() }

    // --- DATASTORE ---
    single<IRepositoryDataStore> { LoginDataStore(androidContext()) }

    // --- GOOGLE AUTH ---
    single { Identity.getSignInClient(androidContext()) }
    single { GoogleAuthUiClient(androidContext(), get()) }

    // --- DATASOURCES ---
    single { LoginRemoteDataSource(get()) }
    single { PetRemoteDataSource() }
    single { StorageDataSource() }

    // --- HELPERS ---
    single { NotificationHelper(androidContext()) }

    // --- REPOSITORIES ---
    single<LoginRepository> { LoginRepositoryImpl(get(), get()) }
    single<PetRepository> { PetRepositoryImpl(get()) }
    single<RewardsRepository> { RewardsRepositoryImpl(get()) }

    // --- USECASES ---
    factory { RegisterUserUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { IsUserLoggedInUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetUserProfileUseCase(get()) }
    factory { ReportPetUseCase(get(), get()) }
    factory { GetUserPetsUseCase(get()) }
    factory { GetPetByIdUseCase(get()) }
    factory { GetAllPetsUseCase(get()) }
    factory { GetAvailableRewardsUseCase(get()) }

    // --- VIEWMODELS ---
    viewModel { LoginViewModel(androidApplication(), get(), get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { RewardsViewModel(get()) }
    viewModel { ReportPetViewModel(androidApplication(), get(), get(), get(), get()) }
    viewModel { PetDetailViewModel(get()) }
}