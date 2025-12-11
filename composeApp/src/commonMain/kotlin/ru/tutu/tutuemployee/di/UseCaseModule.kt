package ru.tutu.tutuemployee.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.tutu.tutuemployee.domain.usecase.auth.GetCurrentUserUseCase
import ru.tutu.tutuemployee.domain.usecase.auth.LoginUseCase
import ru.tutu.tutuemployee.domain.usecase.employee.GetBirthdaysUseCase
import ru.tutu.tutuemployee.domain.usecase.employee.SearchEmployeesUseCase
import ru.tutu.tutuemployee.domain.usecase.news.GetNewsUseCase
import ru.tutu.tutuemployee.domain.usecase.profile.GetAchievementsUseCase
import ru.tutu.tutuemployee.domain.usecase.profile.GetCoursesUseCase
import ru.tutu.tutuemployee.domain.usecase.profile.GetTasksUseCase
import ru.tutu.tutuemployee.domain.usecase.profile.GetVacationsUseCase

/**
 * DI модуль для use cases
 */
val useCaseModule = module {
    // Auth Use Cases
    factoryOf(::LoginUseCase)
    factoryOf(::GetCurrentUserUseCase)

    // News Use Cases
    factoryOf(::GetNewsUseCase)

    // Employee Use Cases
    factoryOf(::GetBirthdaysUseCase)
    factoryOf(::SearchEmployeesUseCase)

    // Profile Use Cases
    factoryOf(::GetAchievementsUseCase)
    factoryOf(::GetTasksUseCase)
    factoryOf(::GetVacationsUseCase)
    factoryOf(::GetCoursesUseCase)
}
