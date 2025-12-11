package ru.tutu.tutuemployee.di

/**
 * Главный DI модуль приложения
 * Объединяет все остальные модули
 */
val appModules = listOf(
    networkModule,
    dataSourceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)
