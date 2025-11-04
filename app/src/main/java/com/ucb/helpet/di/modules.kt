package com.ucb.helpet.di

import com.google.gson.GsonBuilder
import com.ucb.helpet.features.login.data.datasource.ApiService
import com.ucb.helpet.features.login.data.datasource.LoginDataStore
import com.ucb.helpet.features.login.data.datasource.LoginRemoteDataSource
import com.ucb.helpet.features.login.data.repository.LoginRepositoryImpl
import com.ucb.helpet.features.login.domain.repository.IRepositoryDataStore
import com.ucb.helpet.features.login.domain.repository.LoginRepository
import com.ucb.helpet.features.login.domain.usecase.GetTokenUseCase
import com.ucb.helpet.features.login.domain.usecase.RegisterUserUseCase
import com.ucb.helpet.features.login.domain.usecase.SaveTokenUseCase
import com.ucb.helpet.features.login.presentation.LoginViewModel
import com.ucb.helpet.features.login.presentation.register.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://helpet-api-v2-production.up.railway.app/api/v1/") // URL Base de tu API
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    // ApiService
    single<ApiService> { get<Retrofit>().create(ApiService::class.java) }

    // DataStore
    single<IRepositoryDataStore> { LoginDataStore(androidContext()) }

    // RemoteDataSource
    single { LoginRemoteDataSource(get()) }

    // Repositories
    single<LoginRepository> { LoginRepositoryImpl(get()) }

    // UseCases
    factory { GetTokenUseCase(get()) }
    factory { SaveTokenUseCase(get()) }
    factory { RegisterUserUseCase(get()) }

    // ViewModels
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }

}
