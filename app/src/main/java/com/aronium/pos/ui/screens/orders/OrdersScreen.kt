package com.aronium.pos.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aronium.pos.data.model.Order
import com.aronium.pos.data.model.OrderStatus
import com.aronium.pos.ui.viewmodel.OrderViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: OrderViewModel = hiltViewModel()
) {
    val orderState by viewModel.orderState.collectAsState()
    var showNewOrderDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "الطلبات",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { showNewOrderDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "طلب جديد")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Filter
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = orderState.selectedStatus == null,
                    onClick = { viewModel.loadOrdersByStatus(null) },
                    label = { Text("الكل") }
                )
            }
            items(OrderStatus.values()) { status ->
                FilterChip(
                    selected = orderState.selectedStatus == status,
                    onClick = { viewModel.loadOrdersByStatus(status) },
                    label = { Text(getStatusText(status)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Today Filter
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = orderState.isTodayFilter,
                onCheckedChange = { 
                    if (it) {
                        viewModel.loadTodayOrders()
                    } else {
                        viewModel.loadOrdersByStatus(orderState.selectedStatus)
                    }
                }
            )
            Text("طلبات اليوم فقط")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Orders List
        if (orderState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(orderState.orders) { order ->
                    OrderCard(
                        order = order,
                        onStatusChange = { newStatus ->
                            viewModel.updateOrderStatus(order, newStatus)
                        },
                        onDelete = { viewModel.deleteOrder(order) }
                    )
                }
            }
        }
    }

    // New Order Dialog
    if (showNewOrderDialog) {
        NewOrderDialog(
            onDismiss = { showNewOrderDialog = false },
            onAddOrder = { order, orderItems ->
                viewModel.addOrder(order, orderItems)
                showNewOrderDialog = false
            }
        )
    }
}

@Composable
fun OrderCard(
    order: Order,
    onStatusChange: (OrderStatus) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "طلب #${order.orderNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Row {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Order Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "المجموع: ${NumberFormat.getCurrencyInstance(Locale("ar", "SA")).format(order.totalAmount)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "التاريخ: ${SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date(order.createdAt))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                StatusChip(status = order.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status Actions
            if (order.status != OrderStatus.COMPLETED && order.status != OrderStatus.CANCELLED) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (order.status) {
                        OrderStatus.PENDING -> {
                            Button(
                                onClick = { onStatusChange(OrderStatus.PREPARING) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("بدء التحضير")
                            }
                        }
                        OrderStatus.PREPARING -> {
                            Button(
                                onClick = { onStatusChange(OrderStatus.READY) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("جاهز للتسليم")
                            }
                        }
                        OrderStatus.READY -> {
                            Button(
                                onClick = { onStatusChange(OrderStatus.COMPLETED) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("تم التسليم")
                            }
                        }
                        else -> {}
                    }
                    
                    if (order.status != OrderStatus.COMPLETED) {
                        OutlinedButton(
                            onClick = { onStatusChange(OrderStatus.CANCELLED) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("إلغاء")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: OrderStatus) {
    val (text, color) = when (status) {
        OrderStatus.PENDING -> "معلق" to Color(0xFFFF9800)
        OrderStatus.PREPARING -> "قيد التحضير" to Color(0xFF2196F3)
        OrderStatus.READY -> "جاهز" to Color(0xFF4CAF50)
        OrderStatus.COMPLETED -> "مكتمل" to Color(0xFF2E7D32)
        OrderStatus.CANCELLED -> "ملغي" to Color(0xFFF44336)
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun NewOrderDialog(
    onDismiss: () -> Unit,
    onAddOrder: (Order, List<com.aronium.pos.data.model.OrderItem>) -> Unit
) {
    var orderNumber by remember { mutableStateOf("") }
    var totalAmount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("طلب جديد") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = orderNumber,
                    onValueChange = { orderNumber = it },
                    label = { Text("رقم الطلب") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = totalAmount,
                    onValueChange = { totalAmount = it },
                    label = { Text("المجموع") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (orderNumber.isNotBlank() && totalAmount.isNotBlank()) {
                        val order = Order(
                            orderNumber = orderNumber,
                            totalAmount = totalAmount.toDoubleOrNull() ?: 0.0,
                            notes = notes.takeIf { it.isNotBlank() },
                            status = OrderStatus.PENDING
                        )
                        onAddOrder(order, emptyList())
                    }
                }
            ) {
                Text("إضافة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

fun getStatusText(status: OrderStatus): String {
    return when (status) {
        OrderStatus.PENDING -> "معلق"
        OrderStatus.PREPARING -> "قيد التحضير"
        OrderStatus.READY -> "جاهز"
        OrderStatus.COMPLETED -> "مكتمل"
        OrderStatus.CANCELLED -> "ملغي"
    }
}