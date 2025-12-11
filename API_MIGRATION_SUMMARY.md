# API Migration Summary - OpenAPI v1 Specification

## Overview

The API has been adapted to match the provided OpenAPI 3.0.4 specification. The implementation
maintains backward compatibility with existing code while introducing new endpoints according to the
specification.

## Changes Made

### 1. Updated DTOs (Data Transfer Objects)

#### UserDto.kt

- **New Structure**: Changed from flat structure to nested objects following OpenAPI spec
    - `personal`: Contains `firstName`, `lastName`, `middleName`, `email`, `phone`
    - `employment`: Contains `position`, `department`, `location`, `startDate`
    - `manager`: Manager information
    - `roles`: List of user roles
    - `externalIds`: External system IDs (Jira, GitLab, etc.)
- **Added**: `EmployeePreviewDto` for search results
- **Added**: `EmployeeSearchResponse` wrapper for paginated search results

#### NewsDto.kt

- **Added**: `NewsListResponse` - wrapper with total count and items
- **Added**: `NewsPreviewDto` - preview version for list view with `preview_text`
- **Added**: `NewsItemDto` - full news item with `full_text`, tags, and author
- **Added**: `AuthorDto` - author information
- **Kept**: Original `NewsDto` for backward compatibility

#### CommonDto.kt

**Achievements:**

- **Added**: `AchievementListResponse` - wrapper with `user_id`, `total_points`, and items
- **Added**: `AchievementItemDto` - achievement with points field
- **Kept**: Original `AchievementDto` for backward compatibility

**Vacations:**

- **Added**: `VacationListResponse` - wrapper with `employee_id` and items
- **Added**: `VacationPeriodDto` - vacation period with approved_by and status fields
- **Kept**: Original `VacationDto` for backward compatibility

**Favorites:**

- **Added**: `FavoriteListResponse` - wrapper with `user_id` and items
- **Added**: `FavoriteItemDto` - favorite with `link` and `description` fields
- **Added**: `AddFavoriteRequest` - request body for adding favorites
- **Kept**: Original `FavoriteCardDto` for backward compatibility

**Error Handling:**

- **Added**: `ErrorResponse` - standard error response with `error` and `message` fields

### 2. Updated API Service Interface

#### New Endpoints (OpenAPI v1)

```kotlin
// GET /v1/user - получение информации о пользователе
suspend fun getUser(id: String): Result<UserDto>

// GET /v1/news - получение списка новостей с пагинацией и фильтрами
suspend fun getNewsList(
    limit: Int? = null,
    offset: Int? = null,
    category: String? = null,
    search: String? = null
): Result<NewsListResponse>

// GET /v1/news/{id} - получение полной новости
suspend fun getNewsItem(id: String): Result<NewsItemDto>

// GET /v1/employees/search-by-last-name - поиск сотрудников по фамилии
suspend fun searchEmployeesByLastName(
    lastName: String,
    limit: Int? = null,
    offset: Int? = null
): Result<EmployeeSearchResponse>

// GET /v1/vacations - получение отпусков сотрудника
suspend fun getVacations(
    employeeId: String,
    year: Int? = null
): Result<VacationListResponse>

// GET /v1/favorites - получение избранных ссылок
suspend fun getFavorites(userId: String): Result<FavoriteListResponse>

// POST /v1/favorites - добавление избранной ссылки
suspend fun addFavorite(request: AddFavoriteRequest): Result<FavoriteItemDto>

// DELETE /v1/favorites/{id} - удаление избранной ссылки (без изменений)
suspend fun deleteFavorite(id: String): Result<Unit>

// GET /v1/achievements - получение ачивок пользователя
suspend fun getAchievements(userId: String): Result<AchievementListResponse>
```

#### Deprecated Methods (для обратной совместимости)

```kotlin
@Deprecated("Use getUser instead")
suspend fun getCurrentUser(): Result<UserDto>

@Deprecated("Use getNewsList instead")
suspend fun getNews(): Result<List<NewsDto>>

@Deprecated("Use searchEmployeesByLastName instead")
suspend fun searchEmployees(query: String): Result<List<UserDto>>
```

### 3. Updated Data Sources

#### FavoritesRemoteDataSource

- Now uses new `/v1/favorites` endpoint with `userId` parameter
- Converts between `FavoriteItemDto` (API) and `FavoriteCardDto` (domain)
- Transforms `AddFavoriteRequest` from title/url to proper request body

#### ProfileRemoteDataSource

- Now uses `/v1/achievements?user_id=XXX` endpoint
- Converts `AchievementListResponse` to list of `AchievementDto`
- Now uses `/v1/vacations?employee_id=XXX` endpoint
- Converts `VacationListResponse` to list of `VacationDto`

### 4. Implementation Details

#### ApiServiceImpl

- All new endpoints implemented with proper HTTP methods and parameters
- Query parameters correctly set using Ktor's `parameter()` function
- Path parameters embedded in URL strings
- Request bodies serialized with `ContentType.Application.Json`

#### MockApiService

- All new endpoints implemented with mock data
- Proper pagination support in search endpoints
- Filtering by category and search query in news endpoint
- Network delay simulation maintained (500ms)

## Backward Compatibility

All existing code continues to work:

- Old DTO structures remain available
- Deprecated methods still functional
- Data sources automatically adapt old methods to new API
- No changes required in ViewModels or UI layers

## API Endpoints Mapping

| OpenAPI Path | Method | Implementation |
|--------------|--------|----------------|
| `/v1/user` | GET | ✅ `getUser(id)` |
| `/v1/news` | GET | ✅ `getNewsList(...)` |
| `/v1/news/{id}` | GET | ✅ `getNewsItem(id)` |
| `/v1/employees/search-by-last-name` | GET | ✅ `searchEmployeesByLastName(...)` |
| `/v1/vacations` | GET | ✅ `getVacations(...)` |
| `/v1/favorites` | GET | ✅ `getFavorites(userId)` |
| `/v1/favorites` | POST | ✅ `addFavorite(request)` |
| `/v1/favorites/{id}` | DELETE | ✅ `deleteFavorite(id)` |
| `/v1/achievements` | GET | ✅ `getAchievements(userId)` |

## Next Steps

1. **User ID Management**: Currently using hardcoded `"user-123"` in data sources. Should be
   replaced with actual user ID from auth system.

2. **Error Handling**: Consider using `ErrorResponse` DTO for proper error parsing from backend.

3. **Migration**: Gradually update code to use new endpoints:
    - Replace `getCurrentUser()` with `getUser(id)`
    - Replace `getNews()` with `getNewsList()`
    - Replace `searchEmployees()` with `searchEmployeesByLastName()`

4. **Testing**: Add integration tests for new endpoints with proper parameters.

5. **Documentation**: Update API documentation to reflect new endpoints.

## Build Status

✅ **Project builds successfully** with only deprecation warnings for old methods (expected
behavior).

## Notes

- All serial names use snake_case to match API convention (e.g., `@SerialName("user_id")`)
- Response wrappers include metadata (total, items) for pagination
- Date formats preserved as strings (ISO 8601)
- Status enums mapped to backend values ("approved", "pending", "rejected")
