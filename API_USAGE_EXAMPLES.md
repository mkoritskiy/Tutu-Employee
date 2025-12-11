# API Usage Examples - OpenAPI v1

This document provides examples of how to use the new API endpoints.

## 1. Getting User Information

```kotlin
// New API (v1)
suspend fun getUserInfo(apiService: ApiService, userId: String) {
    val result = apiService.getUser(userId)
    result.fold(
        onSuccess = { user ->
            println("User: ${user.personal?.firstName} ${user.personal?.lastName}")
            println("Position: ${user.employment?.position}")
            println("Department: ${user.employment?.department}")
            println("Manager: ${user.manager?.name}")
            println("Roles: ${user.roles.joinToString()}")
        },
        onFailure = { error ->
            println("Error: ${error.message}")
        }
    )
}

// Old API (deprecated but still works)
suspend fun getCurrentUserOld(apiService: ApiService) {
    val result = apiService.getCurrentUser()
    // Still works with old DTO structure
}
```

## 2. Getting News with Filters

```kotlin
// Get latest news with pagination
suspend fun getLatestNews(apiService: ApiService) {
    val result = apiService.getNewsList(
        limit = 10,
        offset = 0,
        category = null,
        search = null
    )
    
    result.onSuccess { response ->
        println("Total news: ${response.total}")
        response.items.forEach { news ->
            println("${news.title}")
            println("  Preview: ${news.previewText}")
            println("  Published: ${news.publishedAt}")
        }
    }
}

// Search news by keyword
suspend fun searchNews(apiService: ApiService, keyword: String) {
    val result = apiService.getNewsList(
        limit = 20,
        offset = 0,
        search = keyword
    )
    
    result.onSuccess { response ->
        println("Found ${response.total} news items matching '$keyword'")
    }
}

// Get full news item
suspend fun getFullNews(apiService: ApiService, newsId: String) {
    val result = apiService.getNewsItem(newsId)
    
    result.onSuccess { news ->
        println("Title: ${news.title}")
        println("Author: ${news.author?.name}")
        println("Tags: ${news.tags.joinToString()}")
        println("\n${news.fullText}")
    }
}
```

## 3. Searching Employees by Last Name

```kotlin
// Search employees by last name
suspend fun findEmployees(apiService: ApiService, lastName: String) {
    val result = apiService.searchEmployeesByLastName(
        lastName = lastName,
        limit = 20,
        offset = 0
    )
    
    result.onSuccess { response ->
        println("Found ${response.total} employees")
        response.items.forEach { employee ->
            println("${employee.firstName} ${employee.lastName}")
            println("  Position: ${employee.position}")
            println("  Department: ${employee.department}")
            println("  Avatar: ${employee.avatarUrl}")
        }
    }
}

// Paginated search
suspend fun findEmployeesPaginated(apiService: ApiService, lastName: String, page: Int) {
    val pageSize = 10
    val result = apiService.searchEmployeesByLastName(
        lastName = lastName,
        limit = pageSize,
        offset = page * pageSize
    )
    
    result.onSuccess { response ->
        val totalPages = (response.total + pageSize - 1) / pageSize
        println("Page ${page + 1} of $totalPages")
        // Display results...
    }
}
```

## 4. Managing Vacations

```kotlin
// Get employee vacations
suspend fun getEmployeeVacations(apiService: ApiService, employeeId: String) {
    val result = apiService.getVacations(
        employeeId = employeeId,
        year = null // Get all years
    )
    
    result.onSuccess { response ->
        println("Vacations for employee ${response.employeeId}:")
        response.items.forEach { vacation ->
            println("${vacation.startDate} - ${vacation.endDate}")
            println("  Type: ${vacation.type}")
            println("  Status: ${vacation.status}")
            println("  Approved by: ${vacation.approvedBy}")
        }
    }
}

// Get vacations for specific year
suspend fun getVacationsForYear(apiService: ApiService, employeeId: String, year: Int) {
    val result = apiService.getVacations(
        employeeId = employeeId,
        year = year
    )
    
    result.onSuccess { response ->
        println("Vacations in $year: ${response.items.size}")
    }
}
```

## 5. Working with Favorites

```kotlin
// Get user favorites
suspend fun getUserFavorites(apiService: ApiService, userId: String) {
    val result = apiService.getFavorites(userId)
    
    result.onSuccess { response ->
        println("Favorites for user ${response.userId}:")
        response.items.forEach { favorite ->
            println("${favorite.description ?: "No description"}")
            println("  Link: ${favorite.link}")
            println("  Created: ${favorite.createdAt}")
        }
    }
}

// Add new favorite
suspend fun addNewFavorite(apiService: ApiService, userId: String, link: String, description: String) {
    val request = AddFavoriteRequest(
        userId = userId,
        link = link,
        description = description
    )
    
    val result = apiService.addFavorite(request)
    
    result.fold(
        onSuccess = { favorite ->
            println("Added: ${favorite.description}")
            println("ID: ${favorite.id}")
        },
        onFailure = { error ->
            println("Failed to add favorite: ${error.message}")
        }
    )
}

// Delete favorite
suspend fun removeFavorite(apiService: ApiService, favoriteId: String) {
    val result = apiService.deleteFavorite(favoriteId)
    
    result.fold(
        onSuccess = {
            println("Favorite deleted successfully")
        },
        onFailure = { error ->
            println("Failed to delete: ${error.message}")
        }
    )
}
```

## 6. Getting User Achievements

```kotlin
// Get achievements with total points
suspend fun getUserAchievements(apiService: ApiService, userId: String) {
    val result = apiService.getAchievements(userId)
    
    result.onSuccess { response ->
        println("User ${response.userId} - Total Points: ${response.totalPoints}")
        println("\nAchievements:")
        response.items.forEach { achievement ->
            println("${achievement.title} (+${achievement.points} points)")
            println("  ${achievement.description}")
            println("  Achieved: ${achievement.achievedAt}")
        }
    }
}

// Calculate achievement progress
suspend fun getAchievementProgress(apiService: ApiService, userId: String) {
    val result = apiService.getAchievements(userId)
    
    result.onSuccess { response ->
        val totalPossiblePoints = 500 // Example
        val percentage = (response.totalPoints.toFloat() / totalPossiblePoints * 100).toInt()
        println("Achievement Progress: $percentage%")
        println("Points: ${response.totalPoints}/$totalPossiblePoints")
    }
}
```

## 7. Complete Example: User Profile Screen

```kotlin
suspend fun loadUserProfile(apiService: ApiService, userId: String) {
    // Load user info
    val userResult = apiService.getUser(userId)
    userResult.onSuccess { user ->
        println("=== User Profile ===")
        println("Name: ${user.personal?.firstName} ${user.personal?.lastName}")
        println("Email: ${user.personal?.email}")
        println("Position: ${user.employment?.position}")
        println("Department: ${user.employment?.department}")
        println()
    }
    
    // Load achievements
    val achievementsResult = apiService.getAchievements(userId)
    achievementsResult.onSuccess { response ->
        println("=== Achievements ===")
        println("Total Points: ${response.totalPoints}")
        println("Achievements: ${response.items.size}")
        response.items.take(3).forEach { achievement ->
            println("  - ${achievement.title} (+${achievement.points})")
        }
        println()
    }
    
    // Load vacations
    val vacationsResult = apiService.getVacations(userId)
    vacationsResult.onSuccess { response ->
        println("=== Vacations ===")
        val upcoming = response.items.filter { it.status == "approved" }
        println("Upcoming vacations: ${upcoming.size}")
        upcoming.take(2).forEach { vacation ->
            println("  ${vacation.startDate} - ${vacation.endDate}")
        }
        println()
    }
    
    // Load favorites
    val favoritesResult = apiService.getFavorites(userId)
    favoritesResult.onSuccess { response ->
        println("=== Quick Links ===")
        response.items.take(5).forEach { favorite ->
            println("  - ${favorite.description}")
        }
    }
}
```

## 8. Error Handling

```kotlin
suspend fun handleApiErrors(apiService: ApiService, userId: String) {
    val result = apiService.getUser(userId)
    
    result.fold(
        onSuccess = { user ->
            // Process user data
            println("Success: ${user.personal?.firstName}")
        },
        onFailure = { error ->
            // Handle different error types
            when {
                error.message?.contains("404") == true -> {
                    println("User not found")
                }
                error.message?.contains("400") == true -> {
                    println("Invalid request")
                }
                error.message?.contains("500") == true -> {
                    println("Server error")
                }
                else -> {
                    println("Unknown error: ${error.message}")
                }
            }
        }
    )
}
```

## Migration Guide

### Replacing Old API Calls

#### 1. User Information

```kotlin
// Old
apiService.getCurrentUser()

// New
apiService.getUser(userId)
```

#### 2. News List

```kotlin
// Old
apiService.getNews()

// New
apiService.getNewsList(limit = 20, offset = 0)
```

#### 3. Employee Search

```kotlin
// Old
apiService.searchEmployees("Иван")

// New
apiService.searchEmployeesByLastName("Иванов", limit = 20, offset = 0)
```

#### 4. Favorites

```kotlin
// Old
apiService.getFavorites()
apiService.addFavorite("Title", "URL")

// New
apiService.getFavorites(userId)
apiService.addFavorite(AddFavoriteRequest(userId, "URL", "Title"))
```

#### 5. Achievements

```kotlin
// Old
apiService.getAchievements()

// New
apiService.getAchievements(userId)
```

#### 6. Vacations

```kotlin
// Old
apiService.getVacations()

// New
apiService.getVacations(employeeId = userId, year = 2025)
```

## Testing with MockApiService

The `MockApiService` provides realistic test data for all new endpoints:

```kotlin
val mockApi = MockApiService()

// All new methods work with mock data
val news = mockApi.getNewsList(limit = 5)
val employees = mockApi.searchEmployeesByLastName("Петр")
val user = mockApi.getUser("user-123")
```

## Notes

- All new endpoints require user/employee ID parameters
- Pagination is supported with `limit` and `offset` parameters
- Search can filter by multiple criteria
- Response wrappers include metadata (total count, etc.)
- All dates are in ISO 8601 format
- Status fields use string enums ("approved", "pending", "rejected")
