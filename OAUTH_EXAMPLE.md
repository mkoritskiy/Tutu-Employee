# OAuth через браузер - Примеры использования

## Базовый пример

### 1. Простой вход через браузер

```kotlin
@Composable
fun MyLoginScreen() {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Кнопка OAuth входа
        Button(onClick = { viewModel.startKeycloakOAuth() }) {
            Icon(Icons.Default.Language, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Войти через Keycloak")
        }
        
        // Показываем ошибку, если есть
        uiState.error?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
    
    // Автоматический переход после успешной авторизации
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            // Переход на главный экран
            navigateToHome()
        }
    }
}
```

### 2. С индикатором загрузки

```kotlin
@Composable
fun EnhancedLoginScreen() {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Логотип
            Icon(
                Icons.Default.Train,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Text(
                "Tutu Employee",
                style = MaterialTheme.typography.headlineLarge
            )
            
            Spacer(Modifier.height(32.dp))
            
            // Кнопка входа
            Button(
                onClick = { viewModel.startKeycloakOAuth() },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Авторизация...")
                } else {
                    Icon(Icons.Default.Language, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Войти через браузер")
                }
            }
            
            // Ошибка
            uiState.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
```

### 3. С выбором метода авторизации

```kotlin
@Composable
fun MultiMethodLoginScreen() {
    val viewModel: AuthViewModel = koinViewModel()
    var showPasswordLogin by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Выберите способ входа",
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(Modifier.height(32.dp))
        
        // OAuth через браузер (рекомендуется)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Безопасный вход", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.Check, contentDescription = "Рекомендуется")
                }
                
                Spacer(Modifier.height(8.dp))
                
                Text(
                    "Через браузер Keycloak",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(Modifier.height(16.dp))
                
                Button(
                    onClick = { viewModel.startKeycloakOAuth() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Language, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Войти через браузер")
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        // Альтернативный метод
        OutlinedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Альтернативный вход",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Spacer(Modifier.height(8.dp))
                
                Text(
                    "Ввод логина и пароля",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = { showPasswordLogin = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Password, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Войти с паролем")
                }
            }
        }
    }
    
    // Dialog для ввода пароля
    if (showPasswordLogin) {
        PasswordLoginDialog(
            onDismiss = { showPasswordLogin = false },
            onLogin = { username, password ->
                viewModel.loginWithKeycloak(username, password)
                showPasswordLogin = false
            }
        )
    }
}
```

## Продвинутые примеры

### 4. С обработкой ошибок

```kotlin
@Composable
fun RobustLoginScreen() {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Показываем ошибки через Snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Long,
                actionLabel = "Повторить"
            ).let { result ->
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.startKeycloakOAuth()
                }
            }
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { viewModel.startKeycloakOAuth() }) {
                Text("Войти через браузер")
            }
        }
    }
}
```

### 5. С аналитикой

```kotlin
@Composable
fun AnalyticsLoginScreen() {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    // Трекинг событий
    LaunchedEffect(Unit) {
        Analytics.logEvent("login_screen_shown")
    }
    
    Button(onClick = {
        Analytics.logEvent("oauth_login_started")
        viewModel.startKeycloakOAuth()
    }) {
        Text("Войти через браузер")
    }
    
    // Трекинг успешной авторизации
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            Analytics.logEvent("login_success", mapOf(
                "method" to "oauth_browser",
                "user_id" to uiState.user?.id
            ))
        }
    }
    
    // Трекинг ошибок
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Analytics.logEvent("login_error", mapOf(
                "method" to "oauth_browser",
                "error" to error
            ))
        }
    }
}
```

### 6. С таймаутом

```kotlin
@Composable
fun TimeoutLoginScreen() {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var timeoutJob by remember { mutableStateOf<Job?>(null) }
    
    // Таймаут для OAuth операции (2 минуты)
    LaunchedEffect(uiState.keycloakAuthUrl) {
        if (uiState.keycloakAuthUrl != null) {
            timeoutJob?.cancel()
            timeoutJob = launch {
                delay(120_000) // 2 минуты
                viewModel.clearKeycloakAuthUrl()
                // Показать ошибку о таймауте
            }
        } else {
            timeoutJob?.cancel()
        }
    }
    
    Button(onClick = { viewModel.startKeycloakOAuth() }) {
        Text("Войти через браузер")
    }
}
```

### 7. С кэшированием сессии

```kotlin
@Composable
fun CachedSessionLoginScreen(
    sessionManager: SessionManager = koinInject()
) {
    val viewModel: AuthViewModel = koinViewModel()
    
    // Проверяем сохраненную сессию при старте
    LaunchedEffect(Unit) {
        if (sessionManager.hasValidSession()) {
            // Автоматически восстанавливаем сессию
            viewModel.restoreSession()
        }
    }
    
    Column {
        Button(onClick = { viewModel.startKeycloakOAuth() }) {
            Text("Войти через браузер")
        }
        
        // Кнопка выхода, если есть сессия
        if (sessionManager.hasValidSession()) {
            TextButton(onClick = {
                viewModel.logout()
                sessionManager.clearSession()
            }) {
                Text("Выйти")
            }
        }
    }
}
```

## Интеграция с Navigation

### 8. С Jetpack Navigation

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    // Автоматическая навигация при авторизации
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = if (uiState.isAuthenticated) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginClick = { viewModel.startKeycloakOAuth() }
            )
        }
        
        composable("home") {
            HomeScreen(
                user = uiState.user,
                onLogout = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
```

## Кастомизация UI

### 9. С брендированным дизайном

```kotlin
@Composable
fun BrandedLoginScreen() {
    val viewModel: AuthViewModel = koinViewModel()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1976D2),
                        Color(0xFF2196F3)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Логотип компании
            Image(
                painter = painterResource(Res.drawable.company_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(Modifier.height(48.dp))
            
            Text(
                "Добро пожаловать!",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                "Войдите для продолжения",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
            
            Spacer(Modifier.height(48.dp))
            
            // Кастомная кнопка
            Surface(
                onClick = { viewModel.startKeycloakOAuth() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Language,
                        contentDescription = null,
                        tint = Color(0xFF1976D2)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Войти через Keycloak",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
```

## Тестирование

### 10. Preview для разработки

```kotlin
@Preview
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}

@Preview
@Composable
fun LoginScreenLoadingPreview() {
    // Mock loading state
    MaterialTheme {
        // Preview with loading state
    }
}

@Preview
@Composable
fun LoginScreenErrorPreview() {
    // Mock error state
    MaterialTheme {
        // Preview with error
    }
}
```

## Best Practices

### ✅ DO

```kotlin
// Используйте OAuth через браузер как основной метод
Button(onClick = { viewModel.startKeycloakOAuth() }) {
    Text("Войти через браузер")
}

// Обрабатывайте ошибки
uiState.error?.let { error ->
    Text(error, color = MaterialTheme.colorScheme.error)
}

// Показывайте loading state
if (uiState.isLoading) {
    CircularProgressIndicator()
}
```

### ❌ DON'T

```kotlin
// Не храните пароли
// ❌ Плохо
val password = remember { mutableStateOf("") }
savePassword(password.value) // НЕ ДЕЛАЙТЕ ТАК!

// Не игнорируйте ошибки
// ❌ Плохо
viewModel.startKeycloakOAuth()
// ... нет обработки ошибок

// Не блокируйте UI
// ❌ Плохо
Button(onClick = {
    runBlocking { // НЕ ДЕЛАЙТЕ ТАК!
        viewModel.startKeycloakOAuth()
    }
})
```

## Заключение

OAuth авторизация через браузер - это **простой, безопасный и удобный** способ входа. Используйте
приведенные примеры как основу для вашей реализации!

**Рекомендуемый подход:** Пример #2 (с индикатором загрузки) + Пример #4 (с обработкой ошибок)
