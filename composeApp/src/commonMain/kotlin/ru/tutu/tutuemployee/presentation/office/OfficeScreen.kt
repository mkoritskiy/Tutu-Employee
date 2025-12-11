package ru.tutu.tutuemployee.presentation.office

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ru.tutu.tutuemployee.domain.model.News
import ru.tutu.tutuemployee.domain.model.WorkspaceBooking
import ru.tutu.tutuemployee.presentation.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficeScreen(
    navController: Any, // Temporary workaround
    onNavigateToWebView: (String, String) -> Unit,
    viewModel: OfficeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Офис") }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = "office"
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
                // Дата
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Бронирование мест",
                            style = MaterialTheme.typography.titleLarge
                        )
                        TextButton(onClick = { /* Открыть календарь */ }) {
                            Text(uiState.selectedDate)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("📅")
                        }
                    }
                }

                // Карта рабочих мест
                item {
                    WorkspacesGrid(
                        workspaces = uiState.workspaceBookings,
                        onBookClick = { workspace ->
                            // Открыть WebView для бронирования
                            onNavigateToWebView(
                                "https://booking.tutu.ru/workspace/${workspace.id}",
                                "Бронирование места ${workspace.workspaceNumber}"
                            )
                        }
                    )
                }

                // Новости офиса
                if (uiState.officeNews.isNotEmpty()) {
                    item {
                        Text(
                            text = "Новости офиса",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(uiState.officeNews) { news ->
                        OfficeNewsCard(news)
                    }
                }
            }
        }
    }
}

@Composable
fun WorkspacesGrid(
    workspaces: List<WorkspaceBooking>,
    onBookClick: (WorkspaceBooking) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(workspaces) { workspace ->
                WorkspaceItem(
                    workspace = workspace,
                    onClick = { onBookClick(workspace) }
                )
            }
        }
    }
}

@Composable
fun WorkspaceItem(
    workspace: WorkspaceBooking,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (workspace.isBooked) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (workspace.isBooked) "✕" else "✓",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = workspace.workspaceNumber,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun OfficeNewsCard(news: News) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
