package ru.tutu.tutuemployee.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.tutu.tutuemployee.domain.model.*
import ru.tutu.tutuemployee.presentation.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: androidx.navigation.NavHostController,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Информация о пользователе
                item {
                    uiState.user?.let { user ->
                        UserInfoCard(user)
                    }
                }

                // Статистика
                item {
                    uiState.user?.let { user ->
                        StatsCard(
                            availableVacationDays = user.availableVacationDays,
                            bonusPoints = user.bonusPoints
                        )
                    }
                }

                // Ачивки
                if (uiState.achievements.isNotEmpty()) {
                    item {
                        Text(
                            text = "Достижения",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    item {
                        AchievementsSection(achievements = uiState.achievements)
                    }
                }

                // Задачи
                if (uiState.tasks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Задачи",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    items(uiState.tasks) { task ->
                        TaskCard(task)
                    }
                }

                // Отпуска
                if (uiState.vacations.isNotEmpty()) {
                    item {
                        Text(
                            text = "Запланированные отпуска",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    items(uiState.vacations) { vacation ->
                        VacationCard(vacation)
                    }
                }

                // Курсы
                if (uiState.courses.isNotEmpty()) {
                    item {
                        Text(
                            text = "Доступные курсы",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    items(uiState.courses) { course ->
                        CourseCard(course)
                    }
                }
            }
        }
    }
}

@Composable
fun UserInfoCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${user.firstName.first()}${user.lastName.first()}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = user.position,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user.department,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user.legalEntity,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StatsCard(availableVacationDays: Int, bonusPoints: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📅",
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$availableVacationDays",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "дней отпуска",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Card(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⭐",
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$bonusPoints",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "бонусов",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun AchievementsSection(achievements: List<Achievement>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(achievements) { achievement ->
            Card(
                modifier = Modifier.width(120.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🏆",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = achievement.title,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (task.status) {
                    TaskStatus.TODO -> "⭕"
                    TaskStatus.IN_PROGRESS -> "⏳"
                    TaskStatus.DONE -> "✅"
                },
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                task.dueDate?.let {
                    Text(
                        text = "Срок: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun VacationCard(vacation: Vacation) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📅",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${vacation.startDate} - ${vacation.endDate}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${vacation.daysCount} дней",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AssistChip(
                onClick = { },
                label = {
                    Text(
                        text = when (vacation.status) {
                            VacationStatus.PLANNED -> "Запланирован"
                            VacationStatus.APPROVED -> "Одобрен"
                            VacationStatus.REJECTED -> "Отклонен"
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun CourseCard(course: Course) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Длительность: ${course.duration}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (course.isCompleted) {
                    Text(
                        text = "✅",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            if (!course.isCompleted && course.progress > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { course.progress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Прогресс: ${course.progress}%",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
