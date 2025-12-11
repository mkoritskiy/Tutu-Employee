package ru.tutu.tutuemployee.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Репозиторий для управления конфигурацией API
 */
interface ApiConfigRepository {
    /**
     * Флаг использования моков
     */
    val useMockApi: StateFlow<Boolean>

    /**
     * Переключить использование моков
     */
    fun toggleApiMode()

    /**
     * Установить режим API
     */
    fun setUseMockApi(useMock: Boolean)
}

/**
 * Реализация репозитория конфигурации API
 */
class ApiConfigRepositoryImpl : ApiConfigRepository {
    private val _useMockApi = MutableStateFlow(true) // По умолчанию используем моки
    override val useMockApi: StateFlow<Boolean> = _useMockApi.asStateFlow()

    override fun toggleApiMode() {
        _useMockApi.value = !_useMockApi.value
    }

    override fun setUseMockApi(useMock: Boolean) {
        _useMockApi.value = useMock
    }
}
