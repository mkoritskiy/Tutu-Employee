package ru.tutu.tutuemployee.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.tutu.tutuemployee.data.repository.*
import ru.tutu.tutuemployee.domain.repository.*

/**
 * DI модуль для репозиториев
 */
val repositoryModule = module {
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::NewsRepositoryImpl) bind NewsRepository::class
    singleOf(::EmployeeRepositoryImpl) bind EmployeeRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    singleOf(::OfficeRepositoryImpl) bind OfficeRepository::class
    singleOf(::MerchRepositoryImpl) bind MerchRepository::class
    singleOf(::FavoritesRepositoryImpl) bind FavoritesRepository::class
}
