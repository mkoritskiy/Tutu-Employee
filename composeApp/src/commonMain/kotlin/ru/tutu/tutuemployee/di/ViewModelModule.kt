package ru.tutu.tutuemployee.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.tutu.tutuemployee.presentation.auth.AuthViewModel
import ru.tutu.tutuemployee.presentation.favorites.FavoritesViewModel
import ru.tutu.tutuemployee.presentation.home.HomeViewModel
import ru.tutu.tutuemployee.presentation.merch.MerchViewModel
import ru.tutu.tutuemployee.presentation.office.OfficeViewModel
import ru.tutu.tutuemployee.presentation.profile.ProfileViewModel

/**
 * DI модуль для ViewModels
 */
val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::OfficeViewModel)
    viewModelOf(::MerchViewModel)
    viewModelOf(::FavoritesViewModel)
}
