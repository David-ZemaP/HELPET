package com.ucb.helpet.di

import com.ucb.helpet.features.login.data.datasource.LoginDataStore
import com.ucb.helpet.features.login.data.repository.RepositoryDataStore
import com.ucb.helpet.features.login.domain.repository.IRepositoryDataStore
import com.ucb.helpet.features.login.domain.usecase.GetTokenUseCase
import com.ucb.helpet.features.login.domain.usecase.SaveTokenUseCase
import com.ucb.helpet.features.login.presentation.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LoginViewModel(get(), get()) }

    single { SaveTokenUseCase(get()) }
    single { GetTokenUseCase(get()) }

    single<IRepositoryDataStore> { RepositoryDataStore(get()) }

    single { LoginDataStore(androidContext()) }
}