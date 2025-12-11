package ru.tutu.tutuemployee.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.tutu.tutuemployee.data.model.Birthday
import ru.tutu.tutuemployee.data.model.News
import ru.tutu.tutuemployee.data.model.User
import ru.tutu.tutuemployee.presentation.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: Any, // Temporary workaround
    onNavigateToWebView: (String, String) -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Главная") }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = "home"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Поиск
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                searchResults = uiState.searchResults,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Дни рождения
                    if (uiState.birthdays.isNotEmpty()) {
                        item {
                            Text(
                                text = "Дни рождения",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        item {
                            BirthdaysSection(birthdays = uiState.birthdays)
                        }
                    }

                    // Новости
                    item {
                        Text(
                            text = "Новости компании",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(uiState.news) { news ->
                        NewsCard(
                            news = news,
                            onClick = { /* Можно открыть детали */ }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<User>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Поиск сотрудников и отделов") },
            leadingIcon = {
                Text("🔍")
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    TextButton(onClick = { onQueryChange("") }) {
                        Text("✕")
                    }
                }
            },
            singleLine = true
        )

        if (searchResults.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column {
                    searchResults.take(5).forEach { user ->
                        ListItem(
                            headlineContent = { Text("${user.firstName} ${user.lastName}") },
                            supportingContent = { Text("${user.position} • ${user.department}") },
                            leadingContent = {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = user.firstName.first().toString(),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.clickable { /* Открыть профиль */ }
                        )
                        if (user != searchResults.take(5).last()) {
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BirthdaysSection(birthdays: List<Birthday>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(birthdays) { birthday ->
            BirthdayCard(birthday)
        }
    }
}

@Composable
fun BirthdayCard(birthday: Birthday) {
    Card(
        modifier = Modifier.width(150.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = birthday.employeeName.split(" ").map { it.first() }.joinToString(""),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = birthday.employeeName,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Text(
                text = birthday.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NewsCard(news: News, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = news.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = news.publishedAt,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
