# Диаграмма архитектуры TutuEmployee

## Общая структура слоев

```
┌─────────────────────────────────────────────────────────────────────┐
│                         PRESENTATION LAYER                          │
│                    (UI, ViewModels, Navigation)                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐            │
│  │  AuthScreen  │  │  HomeScreen  │  │ProfileScreen │  ...        │
│  │              │  │              │  │              │            │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘            │
│         │                 │                 │                      │
│         ↓                 ↓                 ↓                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐            │
│  │AuthViewModel │  │HomeViewModel │  │ProfileViewModel│  ...      │
│  └──────────────┘  └──────────────┘  └──────────────┘            │
│                                                                      │
└──────────────────────────────┬───────────────────────────────────────┘
                               │ Dependency (through Koin DI)
                               ↓
┌─────────────────────────────────────────────────────────────────────┐
│                          DOMAIN LAYER                                │
│              (Business Logic, Models, Interfaces)                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                        Use Cases                              │  │
│  ├──────────────────────────────────────────────────────────────┤  │
│  │  • LoginUseCase                                              │  │
│  │  • GetCurrentUserUseCase                                     │  │
│  │  • GetNewsUseCase                                            │  │
│  │  • GetBirthdaysUseCase                                       │  │
│  │  • SearchEmployeesUseCase                                    │  │
│  │  • GetAchievementsUseCase                                    │  │
│  │  • GetTasksUseCase                                           │  │
│  │  • ...                                                         │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                               ↓                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                   Repository Interfaces                       │  │
│  ├──────────────────────────────────────────────────────────────┤  │
│  │  • AuthRepository                                            │  │
│  │  • NewsRepository                                            │  │
│  │  • EmployeeRepository                                        │  │
│  │  • ProfileRepository                                         │  │
│  │  • OfficeRepository                                          │  │
│  │  • MerchRepository                                           │  │
│  │  • FavoritesRepository                                       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                      Domain Models                            │  │
│  ├──────────────────────────────────────────────────────────────┤  │
│  │  User, News, Birthday, Task, Achievement, Vacation,          │  │
│  │  Course, WorkspaceBooking, MerchItem, FavoriteCard           │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                      Common Classes                           │  │
│  ├──────────────────────────────────────────────────────────────┤  │
│  │  • DomainException (sealed class)                            │  │
│  │  • DomainResult<T> (sealed class)                            │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└──────────────────────────────┬───────────────────────────────────────┘
                               │ Implementation
                               ↓
┌─────────────────────────────────────────────────────────────────────┐
│                           DATA LAYER                                 │
│              (Data Sources, DTOs, Repository Impl)                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                Repository Implementations                     │  │
│  ├──────────────────────────────────────────────────────────────┤  │
│  │  • AuthRepositoryImpl                                        │  │
│  │  • NewsRepositoryImpl                                        │  │
│  │  • EmployeeRepositoryImpl                                    │  │
│  │  • ProfileRepositoryImpl                                     │  │
│  │  • OfficeRepositoryImpl                                      │  │
│  │  • MerchRepositoryImpl                                       │  │
│  │  • FavoritesRepositoryImpl                                   │  │
│  └───────────────────────────┬──────────────────────────────────┘  │
│                               │                                      │
│                               ↓                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                    Remote Data Sources                        │  │
│  ├──────────────────────────────────────────────────────────────┤  │
│  │  • AuthRemoteDataSource                                      │  │
│  │  • NewsRemoteDataSource                                      │  │
│  │  • EmployeeRemoteDataSource                                  │  │
│  │  • ProfileRemoteDataSource                                   │  │
│  │  • OfficeRemoteDataSource                                    │  │
│  │  • MerchRemoteDataSource                                     │  │
│  │  • FavoritesRemoteDataSource                                 │  │
│  └───────────────────────────┬──────────────────────────────────┘  │
│                               │                                      │
│                               ↓                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                       API Service                             │  │
│  ├──────────────────────────────────────────────────────────────┤  │
│  │                      Ktor HttpClient                          │  │
│  │  • ContentNegotiation (JSON)                                 │  │
│  │  • Auth (Bearer Token)                                       │  │
│  │  • Logging                                                    │  │
│  │  • Timeout                                                    │  │
│  └───────────────────────────┬──────────────────────────────────┘  │
│                               │                                      │
│                               ↓                                      │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                    Data Transfer Objects                      │  │
│  ├──────────────────────────────────────────────────────────────┤  │
│  │  UserDto, NewsDto, BirthdayDto, TaskDto,                     │  │
│  │  AchievementDto, VacationDto, CourseDto,                     │  │
│  │  WorkspaceBookingDto, MerchItemDto, FavoriteCardDto          │  │
│  │                                                               │  │
│  │  + Mappers: DTO → Domain Model                               │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└──────────────────────────────┬───────────────────────────────────────┘
                               │
                               ↓
                        ┌──────────────┐
                        │   Backend    │
                        │  REST API    │
                        └──────────────┘
```

## Поток данных

### Загрузка данных (например, новостей)

```
┌─────────────────┐
│   User Action   │  Пользователь открывает экран
│  (открыть экран)│
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│   HomeScreen    │  Composable функция
└────────┬────────┘
         │ collectAsState()
         ↓
┌─────────────────┐
│  HomeViewModel  │  viewModelScope.launch
└────────┬────────┘
         │ invoke()
         ↓
┌─────────────────┐
│  GetNewsUseCase │  Валидация, бизнес-логика
└────────┬────────┘
         │ getNews()
         ↓
┌─────────────────┐
│  NewsRepository │  Interface (Domain)
│   (Interface)   │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│NewsRepositoryImpl│ Реализация (Data)
└────────┬────────┘
         │ getNews()
         ↓
┌─────────────────┐
│NewsRemoteDataS..│  Data Source
└────────┬────────┘
         │ GET /news
         ↓
┌─────────────────┐
│   ApiService    │  Ktor HttpClient
└────────┬────────┘
         │ HTTP Request
         ↓
┌─────────────────┐
│    Backend      │  REST API
│  (https://...)  │
└────────┬────────┘
         │ JSON Response
         ↓
┌─────────────────┐
│   List<NewsDto> │  Data Transfer Object
└────────┬────────┘
         │ .map { it.toDomain() }
         ↓
┌─────────────────┐
│   List<News>    │  Domain Model
└────────┬────────┘
         │ Result.success(news)
         ↓
┌─────────────────┐
│  HomeViewModel  │  Обновляет uiState
│   _uiState.value│
└────────┬────────┘
         │ StateFlow
         ↓
┌─────────────────┐
│   HomeScreen    │  Recomposition
│  (отображение)  │
└─────────────────┘
```

## Dependency Injection (Koin)

```
┌─────────────────────────────────────────────────────────────┐
│                       Koin Container                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌────────────────┐                                         │
│  │ NetworkModule  │                                         │
│  ├────────────────┤                                         │
│  │ • HttpClient   │ ← single                               │
│  │ • ApiService   │ ← single                               │
│  │ • TokenStorage │ ← single                               │
│  └────────────────┘                                         │
│         ↓                                                    │
│  ┌────────────────┐                                         │
│  │DataSourceModule│                                         │
│  ├────────────────┤                                         │
│  │ • AuthRDS      │ ← single (bind interface)              │
│  │ • NewsRDS      │ ← single (bind interface)              │
│  │ • EmployeeRDS  │ ← single (bind interface)              │
│  │ • ProfileRDS   │ ← single (bind interface)              │
│  │ • OfficeRDS    │ ← single (bind interface)              │
│  │ • MerchRDS     │ ← single (bind interface)              │
│  │ • FavoritesRDS │ ← single (bind interface)              │
│  └────────────────┘                                         │
│         ↓                                                    │
│  ┌────────────────┐                                         │
│  │RepositoryModule│                                         │
│  ├────────────────┤                                         │
│  │ • AuthRepo     │ ← single (bind interface)              │
│  │ • NewsRepo     │ ← single (bind interface)              │
│  │ • EmployeeRepo │ ← single (bind interface)              │
│  │ • ProfileRepo  │ ← single (bind interface)              │
│  │ • OfficeRepo   │ ← single (bind interface)              │
│  │ • MerchRepo    │ ← single (bind interface)              │
│  │ • FavoritesRepo│ ← single (bind interface)              │
│  └────────────────┘                                         │
│         ↓                                                    │
│  ┌────────────────┐                                         │
│  │ UseCaseModule  │                                         │
│  ├────────────────┤                                         │
│  │ • LoginUseCase │ ← factory                              │
│  │ • GetNewsUC    │ ← factory                              │
│  │ • GetBirthdaysUC│ ← factory                             │
│  │ • ... (все UC) │ ← factory                              │
│  └────────────────┘                                         │
│         ↓                                                    │
│  ┌────────────────┐                                         │
│  │ViewModelModule │                                         │
│  ├────────────────┤                                         │
│  │ • AuthVM       │ ← viewModelOf                          │
│  │ • HomeVM       │ ← viewModelOf                          │
│  │ • ProfileVM    │ ← viewModelOf                          │
│  │ • OfficeVM     │ ← viewModelOf                          │
│  │ • MerchVM      │ ← viewModelOf                          │
│  │ • FavoritesVM  │ ← viewModelOf                          │
│  └────────────────┘                                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## Пример связей для AuthViewModel

```
AuthViewModel
    ↓ (constructor injection)
LoginUseCase
    ↓ (constructor injection)
AuthRepository (interface)
    ↑ (implementation)
AuthRepositoryImpl
    ↓ (constructor injection)
    ├─→ AuthRemoteDataSource (interface)
    │       ↑ (implementation)
    │   AuthRemoteDataSourceImpl
    │       ↓ (constructor injection)
    │   ApiService
    │       ↓ (uses)
    │   HttpClient (Ktor)
    │
    └─→ TokenStorage (interface)
            ↑ (implementation)
        InMemoryTokenStorage
```

## Схема модулей проекта

```
composeApp
│
├── presentation/                    # UI Layer
│   ├── auth/
│   │   ├── AuthScreen.kt           ← Composable
│   │   └── AuthViewModel.kt        ← ViewModel (Koin)
│   ├── home/
│   ├── profile/
│   ├── office/
│   ├── merch/
│   ├── favorites/
│   ├── components/                 ← Reusable UI
│   └── navigation/                 ← Navigation
│
├── domain/                          # Business Logic Layer
│   ├── model/                      ← Domain Models (POJO)
│   ├── repository/                 ← Repository Interfaces
│   ├── usecase/                    ← Business Use Cases
│   └── common/                     ← Exceptions, Result
│
├── data/                            # Data Layer
│   ├── repository/                 ← Repository Implementations
│   └── remote/
│       ├── api/                    ← HTTP Client (Ktor)
│       ├── dto/                    ← DTOs + Mappers
│       └── datasource/             ← Data Source Abstractions
│
└── di/                              # Dependency Injection
    ├── AppModule.kt                ← Main module
    ├── NetworkModule.kt            ← HTTP, Auth
    ├── DataSourceModule.kt         ← Data Sources
    ├── RepositoryModule.kt         ← Repositories
    ├── UseCaseModule.kt            ← Use Cases
    └── ViewModelModule.kt          ← ViewModels
```

## Легенда

- `→` : Зависимость (depends on)
- `↓` : Поток данных (data flow)
- `↑` : Реализация (implements)
- `←` : Инжектируется через (injected by)

RDS = RemoteDataSource  
UC = UseCase  
VM = ViewModel  
Repo = Repository
