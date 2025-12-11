# API Models Documentation

## Data Models

### User

```kotlin
@Serializable
data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val position: String,           // Должность
    val department: String,          // Отдел
    val legalEntity: String,         // Юридическое лицо
    val email: String,
    val avatarUrl: String? = null,
    val availableVacationDays: Int = 0,
    val bonusPoints: Int = 0         // Внутренние баллы
)
```

### News

```kotlin
@Serializable
data class News(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val publishedAt: String,
    val category: NewsCategory = NewsCategory.COMPANY
)

enum class NewsCategory {
    COMPANY,    // Новости компании
    OFFICE,     // Новости офиса
    EVENTS      // События
}
```

### Birthday

```kotlin
@Serializable
data class Birthday(
    val employeeId: String,
    val employeeName: String,
    val date: String,               // Формат: "dd.MM"
    val department: String,
    val avatarUrl: String? = null
)
```

### Achievement

```kotlin
@Serializable
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconUrl: String? = null,
    val earnedAt: String            // ISO 8601 date
)
```

### Task

```kotlin
@Serializable
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val dueDate: String? = null     // ISO 8601 date
)

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}
```

### Vacation

```kotlin
@Serializable
data class Vacation(
    val id: String,
    val startDate: String,          // ISO 8601 date
    val endDate: String,            // ISO 8601 date
    val daysCount: Int,
    val status: VacationStatus,
    val reason: String? = null
)

enum class VacationStatus {
    PLANNED,    // Запланирован
    APPROVED,   // Одобрен
    REJECTED    // Отклонен
}
```

### Course

```kotlin
@Serializable
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val duration: String,           // Например: "2 hours", "5 days"
    val imageUrl: String? = null,
    val progress: Int = 0,          // 0-100
    val isCompleted: Boolean = false
)
```

### WorkspaceBooking

```kotlin
@Serializable
data class WorkspaceBooking(
    val id: String,
    val workspaceNumber: String,    // Например: "A-15"
    val date: String,               // ISO 8601 date
    val isBooked: Boolean,
    val bookedBy: String? = null,   // User ID или имя
    val floor: Int
)
```

### MerchItem

```kotlin
@Serializable
data class MerchItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,                 // Цена в баллах
    val imageUrl: String? = null,
    val category: MerchCategory,
    val inStock: Boolean = true
)

enum class MerchCategory {
    CLOTHING,       // Одежда
    ACCESSORIES,    // Аксессуары
    STATIONERY,     // Канцелярия
    ELECTRONICS     // Электроника
}
```

### FavoriteCard

```kotlin
@Serializable
data class FavoriteCard(
    val id: String,
    val title: String,
    val url: String,
    val iconUrl: String? = null
)
```

### Auth Models

```kotlin
@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,              // Bearer token
    val user: User
)
```

## API Responses

### Success Response

Все успешные запросы возвращают данные напрямую в формате JSON.

### Error Response

```json
{
  "error": "Error message",
  "code": 400
}
```

## Common HTTP Status Codes

- `200 OK` - Успешный запрос
- `201 Created` - Ресурс создан
- `400 Bad Request` - Неверный запрос
- `401 Unauthorized` - Требуется авторизация
- `403 Forbidden` - Доступ запрещен
- `404 Not Found` - Ресурс не найден
- `500 Internal Server Error` - Ошибка сервера

## Date Formats

- **ISO 8601**: `2024-01-15T10:30:00Z` (для дат с временем)
- **Date only**: `2024-01-15` (только дата)
- **Birthday**: `15.01` (день и месяц)

## Authentication

Все запросы (кроме `/auth/login`) требуют Bearer токен в заголовке:

```
Authorization: Bearer <token>
```

## Pagination (будущая функциональность)

```kotlin
data class PaginatedResponse<T>(
    val items: List<T>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)
```

## Example API Requests

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "ivan.ivanov",
  "password": "password123"
}
```

### Get News

```http
GET /news
Authorization: Bearer <token>
```

### Search Employees

```http
GET /search/employees?q=иван
Authorization: Bearer <token>
```

### Purchase Merch

```http
POST /merch/123/purchase
Authorization: Bearer <token>
```

### Add Favorite

```http
POST /favorites
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Jira",
  "url": "https://jira.tutu.ru"
}
```
