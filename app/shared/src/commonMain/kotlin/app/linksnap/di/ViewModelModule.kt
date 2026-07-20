package app.linksnap.di

import app.linksnap.features.auth.presentation.AuthViewModel
import app.linksnap.features.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun viewModelModule()  = module {

    viewModel { AuthViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
}