package ru.tutu.tutuemployee.data.remote.api

/**
 * Примеры использования Mock API
 * Этот файл можно удалить - он только для демонстрации
 */

/*
// Пример 1: Авторизация
suspend fun exampleLogin(apiService: ApiService) {
    val result = apiService.login("ivan.ivanov", "password123")
    result.fold(
        onSuccess = { authResponse ->
            println("Успешная авторизация!")
            println("Token: ${authResponse.token}")
            println("User: ${authResponse.user.firstName} ${authResponse.user.lastName}")
            println("Баллы: ${authResponse.user.bonusPoints}")
        },
        onFailure = { error ->
            println("Ошибка: ${error.message}")
        }
    )
}

// Пример 2: Получение новостей
suspend fun exampleGetNews(apiService: ApiService) {
    val result = apiService.getNews()
    result.fold(
        onSuccess = { newsList ->
            println("Получено ${newsList.size} новостей:")
            newsList.forEach { news ->
                println("- ${news.title} (${news.category})")
            }
        },
        onFailure = { error ->
            println("Ошибка: ${error.message}")
        }
    )
}

// Пример 3: Поиск сотрудников
suspend fun exampleSearchEmployees(apiService: ApiService) {
    // Поиск по имени
    val result1 = apiService.searchEmployees("Мария")
    result1.onSuccess { employees ->
        println("Найдено ${employees.size} сотрудников:")
        employees.forEach { emp ->
            println("${emp.firstName} ${emp.lastName} - ${emp.position}")
        }
    }
    
    // Поиск по должности
    val result2 = apiService.searchEmployees("Developer")
    result2.onSuccess { employees ->
        println("\nРазработчики:")
        employees.forEach { emp ->
            println("${emp.firstName} ${emp.lastName} - ${emp.department}")
        }
    }
    
    // Все сотрудники (пустой запрос)
    val result3 = apiService.searchEmployees("")
    result3.onSuccess { employees ->
        println("\nВсего сотрудников: ${employees.size}")
    }
}

// Пример 4: Покупка мерча
suspend fun examplePurchaseMerch(apiService: ApiService) {
    // Сначала получаем текущего пользователя
    val userResult = apiService.getCurrentUser()
    userResult.onSuccess { user ->
        println("Баллов доступно: ${user.bonusPoints}")
    }
    
    // Получаем список товаров
    val merchResult = apiService.getMerchItems()
    merchResult.onSuccess { items ->
        println("\nДоступные товары:")
        items.filter { it.inStock }.forEach { item ->
            println("${item.name} - ${item.price} баллов")
        }
    }
    
    // Покупаем футболку (100 баллов)
    val purchaseResult = apiService.purchaseMerchItem("merch_1")
    purchaseResult.fold(
        onSuccess = {
            println("\n[SUCCESS] Покупка успешна!")
            // Проверяем новый баланс
            apiService.getCurrentUser().onSuccess { user ->
                println("Остаток баллов: ${user.bonusPoints}")
            }
        },
        onFailure = { error ->
            println("[ERROR] Ошибка покупки: ${error.message}")
        }
    )
}

// Пример 5: Работа с избранным
suspend fun exampleFavorites(apiService: ApiService) {
    // Получить текущий список
    val result1 = apiService.getFavorites()
    result1.onSuccess { favorites ->
        println("Избранное (${favorites.size}):")
        favorites.forEach { fav ->
            println("- ${fav.title}: ${fav.url}")
        }
    }
    
    // Добавить новую карточку
    val result2 = apiService.addFavorite("Slack", "https://slack.tutu.ru")
    result2.onSuccess { newFav ->
        println("\n[SUCCESS] Добавлено: ${newFav.title}")
    }
    
    // Получить обновленный список
    val result3 = apiService.getFavorites()
    result3.onSuccess { favorites ->
        println("\nОбновленное избранное (${favorites.size}):")
        favorites.forEach { fav ->
            println("- ${fav.title}")
        }
    }
    
    // Удалить карточку
    val result4 = apiService.deleteFavorite("fav_1")
    result4.onSuccess {
        println("\n[SUCCESS] Карточка удалена")
    }
}

// Пример 6: Профиль пользователя
suspend fun exampleProfile(apiService: ApiService) {
    // Достижения
    val achievements = apiService.getAchievements()
    achievements.onSuccess { list ->
        println("[TROPHY] Достижения (${list.size}):")
        list.forEach { ach ->
            println("  ${ach.iconUrl} ${ach.title}")
        }
    }
    
    // Задачи
    val tasks = apiService.getTasks()
    tasks.onSuccess { list ->
        println("\n[LIST] Задачи:")
        list.groupBy { it.status }.forEach { (status, tasks) ->
            println("  $status: ${tasks.size}")
        }
    }
    
    // Отпуска
    val vacations = apiService.getVacations()
    vacations.onSuccess { list ->
        println("\n[VACATION] Отпуска (${list.size}):")
        list.forEach { vac ->
            println("  ${vac.startDate} - ${vac.endDate} (${vac.daysCount} дней)")
            println("  Статус: ${vac.status}")
        }
    }
    
    // Курсы
    val courses = apiService.getCourses()
    courses.onSuccess { list ->
        println("\n[COURSE] Курсы (${list.size}):")
        list.forEach { course ->
            val status = if (course.isCompleted) "[DONE]" else "${course.progress}%"
            println("  $status ${course.title}")
        }
    }
}

// Пример 7: Офис
suspend fun exampleOffice(apiService: ApiService) {
    // Бронирование рабочих мест
    val workspaces = apiService.getWorkspaceBookings("2024-01-16")
    workspaces.onSuccess { list ->
        println("Рабочие места на 16.01.2024:")
        list.groupBy { it.floor }.forEach { (floor, spaces) ->
            println("\nЭтаж $floor:")
            spaces.forEach { ws ->
                val status = if (ws.isBooked) "[BOOKED] ${ws.bookedBy}" else "[FREE] Свободно"
                println("  ${ws.workspaceNumber}: $status")
            }
        }
    }
    
    // Новости офиса
    val news = apiService.getOfficeNews()
    news.onSuccess { list ->
        println("\n\nНовости офиса (${list.size}):")
        list.forEach { n ->
            println("- ${n.title}")
        }
    }
}

// Пример 8: Дни рождения
suspend fun exampleBirthdays(apiService: ApiService) {
    val result = apiService.getBirthdays()
    result.onSuccess { birthdays ->
        println("[BIRTHDAY] Ближайшие дни рождения:")
        birthdays.forEach { birthday ->
            println("  ${birthday.date} - ${birthday.employeeName} (${birthday.department})")
        }
    }
}
*/
