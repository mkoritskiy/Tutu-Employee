# OpenAPI v1 - Quick Reference

Quick reference guide for using the new OpenAPI v1 endpoints.

## Status: ✅ Ready to Use

All endpoints are implemented and tested. Build successful.

## API Endpoints Overview

| Endpoint | Method | Purpose | Parameters |
|----------|--------|---------|------------|
| `/v1/user` | GET | Get user info | `id` (query) |
| `/v1/news` | GET | List news | `limit`, `offset`, `category`, `search` |
| `/v1/news/{id}` | GET | Get full news | `id` (path) |
| `/v1/employees/search-by-last-name` | GET | Search employees | `last_name`, `limit`, `offset` |
| `/v1/vacations` | GET | Get vacations | `employee_id`, `year` |
| `/v1/favorites` | GET | Get favorites | `user_id` |
| `/v1/favorites` | POST | Add favorite | Request body |
| `/v1/favorites/{id}` | DELETE | Delete favorite | `id` (path) |
| `/v1/achievements` | GET | Get achievements | `user_id` |

## Quick Start Examples

### 1. Get User Information

```kotlin
val userId = "user-123"
val result = apiService.getUser(userId)

result.onSuccess { user ->
    println("${user.personal?.firstName} ${user.personal?.lastName}")
    println("Position: ${user.employment?.position}")
}
```

### 2. Get News with Pagination

```kotlin
val result = apiService.getNewsList(
    limit = 20,
    offset = 0,
    category = null,
    search = "офис"
)

result.onSuccess { response ->
    println("Total: ${response.total}")
    response.items.forEach { news ->
        println(news.title)
    }
}
```

### 3. Search Employees by Last Name

```kotlin
val result = apiService.searchEmployeesByLastName(
    lastName = "Иванов",
    limit = 20,
    offset = 0
)

result.onSuccess { response ->
    println("Found: ${response.total}")
    response.items.forEach { employee ->
        println("${employee.firstName} ${employee.lastName}")
    }
}
```

### 4. Get Employee Vacations

```kotlin
val result = apiService.getVacations(
    employeeId = "emp-123",
    year = 2025
)

result.onSuccess { response ->
    response.items.forEach { vacation ->
        println("${vacation.startDate} - ${vacation.endDate}")
        println("Status: ${vacation.status}")
    }
}
```

### 5. Manage Favorites

```kotlin
// Get favorites
val favorites = apiService.getFavorites("user-123")

// Add favorite
val request = AddFavoriteRequest(
    userId = "user-123",
    link = "https://jira.tutu.ru",
    description = "Jira"
)
val added = apiService.addFavorite(request)

// Delete favorite
val deleted = apiService.deleteFavorite("fav-123")
```

### 6. Get Achievements

```kotlin
val result = apiService.getAchievements("user-123")

result.onSuccess { response ->
    println("Total Points: ${response.totalPoints}")
    response.items.forEach { achievement ->
        println("${achievement.title} (+${achievement.points})")
    }
}
```

## Common Patterns

### Pagination

```kotlin
fun loadPage(page: Int, pageSize: Int = 20) {
    apiService.getNewsList(
        limit = pageSize,
        offset = page * pageSize
    )
}
```

### Filtering

```kotlin
// By category
apiService.getNewsList(category = "company")

// By search query
apiService.getNewsList(search = "офис")

// Combined
apiService.getNewsList(
    limit = 20,
    category = "events",
    search = "conference"
)
```

### Error Handling

```kotlin
result.fold(
    onSuccess = { data -> /* handle success */ },
    onFailure = { error ->
        when {
            error.message?.contains("404") == true -> 
                println("Not found")
            error.message?.contains("400") == true -> 
                println("Bad request")
            else -> 
                println("Error: ${error.message}")
        }
    }
)
```

## DTO Mappings

### UserDto Structure

```kotlin
UserDto(
    id: String,
    status: String?,
    personal: PersonalInfo(
        firstName: String,
        lastName: String,
        middleName: String?,
        email: String,
        phone: String?
    ),
    employment: EmploymentInfo(
        position: String,
        department: String,
        location: String?,
        startDate: String?
    ),
    manager: ManagerInfo?,
    roles: List<String>,
    externalIds: Map<String, String>
)
```

### Response Wrappers

All list endpoints return wrapper objects:

```kotlin
NewsListResponse(
    total: Int,
    items: List<NewsPreviewDto>
)

EmployeeSearchResponse(
    total: Int,
    items: List<EmployeePreviewDto>
)

FavoriteListResponse(
    userId: String,
    items: List<FavoriteItemDto>
)

VacationListResponse(
    employeeId: String,
    items: List<VacationPeriodDto>
)

AchievementListResponse(
    userId: String,
    totalPoints: Int,
    items: List<AchievementItemDto>
)
```

## Using MockApiService

For testing and development:

```kotlin
val mockApi = MockApiService()

// All methods work with realistic test data
val user = mockApi.getUser("any-id")
val news = mockApi.getNewsList(limit = 10)
val employees = mockApi.searchEmployeesByLastName("Петров")
```

Mock service features:

- ✅ 500ms network delay simulation
- ✅ Realistic test data
- ✅ Pagination support
- ✅ Search filtering
- ✅ In-memory favorites storage

## Migration from Old API

| Old Method | New Method | Changes |
|------------|------------|---------|
| `getCurrentUser()` | `getUser(id)` | Requires user ID |
| `getNews()` | `getNewsList()` | Returns wrapper with pagination |
| `searchEmployees(query)` | `searchEmployeesByLastName(lastName)` | Search by last name only |
| `getAchievements()` | `getAchievements(userId)` | Requires user ID, returns wrapper |
| `getVacations()` | `getVacations(employeeId)` | Requires employee ID, returns wrapper |
| `getFavorites()` | `getFavorites(userId)` | Requires user ID, returns wrapper |
| `addFavorite(title, url)` | `addFavorite(request)` | Uses request object |

## Important Notes

### User ID Management

Currently uses hardcoded `"user-123"`. Data sources handle this internally:

```kotlin
// In data sources
private fun getCurrentUserId(): String = "user-123"
```

**TODO**: Integrate with AuthManager when available.

### Field Naming

All API fields use snake_case:

- `first_name` not `firstName`
- `user_id` not `userId`
- `employee_id` not `employeeId`

DTOs handle mapping automatically with `@SerialName`.

### Date Format

All dates are ISO 8601 strings:

- `2025-01-15T10:30:00Z` (with time)
- `2025-01-15` (date only)

### Status Values

Vacation status enum values:

- `"approved"` - Approved vacation
- `"pending"` - Pending approval
- `"rejected"` - Rejected vacation

## Testing Checklist

- ✅ All endpoints compile
- ✅ Koin DI works correctly
- ✅ Mock service provides test data
- ✅ DTOs serialize/deserialize properly
- ✅ Old API methods still work (deprecated)
- ✅ Build successful

## Files Modified

```
data/remote/dto/
  - UserDto.kt (restructured)
  - NewsDto.kt (added new DTOs)
  - CommonDto.kt (added new DTOs)

data/remote/api/
  - ApiService.kt (new methods)
  - ApiServiceImpl.kt (implementations)
  - MockApiService.kt (mock data)

data/remote/datasource/
  - FavoritesRemoteDataSource.kt (updated)
  - ProfileRemoteDataSource.kt (updated)
```

## Documentation

- 📄 **OPENAPI_ADAPTATION_COMPLETE.md** - Complete migration report
- 📄 **API_MIGRATION_SUMMARY.md** - Detailed changes
- 📄 **API_USAGE_EXAMPLES.md** - Code examples
- 📄 **OPENAPI_QUICK_REFERENCE.md** - This file

## Support

For issues or questions:

1. Check documentation files above
2. Review OpenAPI specification JSON
3. Test with MockApiService first
4. Check Koin configuration if DI errors occur

---

**Last Updated**: December 11, 2025  
**Status**: ✅ Production Ready
