package ru.tutu.tutuemployee.presentation.merch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import ru.tutu.tutuemployee.domain.model.MerchCategory
import ru.tutu.tutuemployee.domain.model.MerchItem
import ru.tutu.tutuemployee.presentation.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchScreen(
    navController: NavHostController,
    viewModel: MerchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showPurchaseDialog by remember { mutableStateOf<MerchItem?>(null) }

    // Показываем сообщения
    LaunchedEffect(uiState.purchaseSuccess, uiState.error) {
        if (uiState.purchaseSuccess != null || uiState.error != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Магазин мерча",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    // Баланс баллов
                    Surface(
                        modifier = Modifier.padding(8.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "${uiState.userPoints}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        },
        snackbarHost = {
            if (uiState.purchaseSuccess != null) {
                Snackbar {
                    Text(uiState.purchaseSuccess ?: "")
                }
            } else if (uiState.error != null) {
                Snackbar {
                    Text(uiState.error ?: "")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Фильтр по категориям
            CategoryFilter(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::selectCategory
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val filteredItems = if (uiState.selectedCategory != null) {
                    uiState.items.filter { it.category == uiState.selectedCategory }
                } else {
                    uiState.items
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredItems) { item ->
                        MerchItemCard(
                            item = item,
                            userPoints = uiState.userPoints,
                            onClick = { showPurchaseDialog = item }
                        )
                    }
                }
            }
        }
    }

    // Диалог подтверждения покупки
    showPurchaseDialog?.let { item ->
        PurchaseDialog(
            item = item,
            userPoints = uiState.userPoints,
            onConfirm = {
                viewModel.purchaseItem(item.id)
                showPurchaseDialog = null
            },
            onDismiss = { showPurchaseDialog = null }
        )
    }
}

@Composable
fun CategoryFilter(
    selectedCategory: MerchCategory?,
    onCategorySelected: (MerchCategory?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Все") }
            )
        }

        items(MerchCategory.entries) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        when (category) {
                            MerchCategory.CLOTHING -> "Одежда"
                            MerchCategory.ACCESSORIES -> "Аксессуары"
                            MerchCategory.STATIONERY -> "Канцелярия"
                            MerchCategory.ELECTRONICS -> "Электроника"
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun MerchItemCard(
    item: MerchItem,
    userPoints: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Заглушка для изображения
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.CardGiftcard,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (userPoints >= item.price) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                    Text(
                        text = "${item.price}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (userPoints >= item.price) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                }

                if (!item.inStock) {
                    Text(
                        text = "Нет в наличии",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun PurchaseDialog(
    item: MerchItem,
    userPoints: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val canPurchase = userPoints >= item.price && item.inStock

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтверждение покупки") },
        text = {
            Column {
                Text("Вы уверены, что хотите приобрести:")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Стоимость:")
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("${item.price}")
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ваш баланс:")
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("$userPoints")
                }

                if (!canPurchase) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (!item.inStock) "Товар отсутствует в наличии" else "Недостаточно баллов",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = canPurchase
            ) {
                Text("Купить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
