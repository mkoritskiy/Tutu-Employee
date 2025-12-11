package ru.tutu.tutuemployee.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.tutu.tutuemployee.data.remote.datasource.*

/**
 * DI модуль для data sources
 */
val dataSourceModule = module {
    // Auth Data Source
    singleOf(::AuthRemoteDataSourceImpl) bind AuthRemoteDataSource::class

    // News Data Source
    singleOf(::NewsRemoteDataSourceImpl) bind NewsRemoteDataSource::class

    // Employee Data Source
    singleOf(::EmployeeRemoteDataSourceImpl) bind EmployeeRemoteDataSource::class

    // Profile Data Source
    singleOf(::ProfileRemoteDataSourceImpl) bind ProfileRemoteDataSource::class

    // Office Data Source
    singleOf(::OfficeRemoteDataSourceImpl) bind OfficeRemoteDataSource::class

    // Merch Data Source
    singleOf(::MerchRemoteDataSourceImpl) bind MerchRemoteDataSource::class

    // Favorites Data Source
    singleOf(::FavoritesRemoteDataSourceImpl) bind FavoritesRemoteDataSource::class
}
