package com.aronium.pos.ui.screens.reports

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen() {
    var selectedReportType by remember { mutableStateOf(ReportType.SALES) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "التقارير",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Report Type Filter
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(ReportType.values()) { reportType ->
                FilterChip(
                    selected = selectedReportType == reportType,
                    onClick = { selectedReportType = reportType },
                    label = { Text(getReportTypeText(reportType)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Report Content
        when (selectedReportType) {
            ReportType.SALES -> SalesReport()
            ReportType.PRODUCTS -> ProductsReport()
            ReportType.CUSTOMERS -> CustomersReport()
            ReportType.DAILY -> DailyReport()
            ReportType.WEEKLY -> WeeklyReport()
            ReportType.MONTHLY -> MonthlyReport()
        }
    }
}

@Composable
fun SalesReport() {
    Column {
        Text(
            text = "تقرير المبيعات",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Sales Stats Cards
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ReportCard(
                    title = "إجمالي المبيعات",
                    value = NumberFormat.getCurrencyInstance(Locale("ar", "SA")).format(12500.0),
                    icon = Icons.Default.AttachMoney,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                ReportCard(
                    title = "عدد الطلبات",
                    value = "156",
                    icon = Icons.Default.ShoppingCart,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            item {
                ReportCard(
                    title = "متوسط قيمة الطلب",
                    value = NumberFormat.getCurrencyInstance(Locale("ar", "SA")).format(80.13),
                    icon = Icons.Default.TrendingUp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sales Chart Placeholder
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.BarChart,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "رسم بياني للمبيعات",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ProductsReport() {
    Column {
        Text(
            text = "تقرير المنتجات",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Top Products
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "أكثر المنتجات مبيعاً",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sample data
                val topProducts = listOf(
                    "برجر لحم" to 45,
                    "بيتزا مارجريتا" to 38,
                    "سلطة سيزر" to 32,
                    "مشروب غازي" to 28,
                    "شوربة دجاج" to 25
                )
                
                topProducts.forEach { (product, sales) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(product)
                        Text("$sales مبيعة")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomersReport() {
    Column {
        Text(
            text = "تقرير العملاء",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Customer Stats
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ReportCard(
                    title = "إجمالي العملاء",
                    value = "1,234",
                    icon = Icons.Default.People,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                ReportCard(
                    title = "عملاء جدد هذا الشهر",
                    value = "89",
                    icon = Icons.Default.PersonAdd,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            item {
                ReportCard(
                    title = "العملاء النشطين",
                    value = "456",
                    icon = Icons.Default.Star,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun DailyReport() {
    Column {
        Text(
            text = "التقرير اليومي",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val today = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
        
        Text(
            text = "تاريخ: $today",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Daily Stats
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ReportCard(
                    title = "مبيعات اليوم",
                    value = NumberFormat.getCurrencyInstance(Locale("ar", "SA")).format(1250.0),
                    icon = Icons.Default.AttachMoney,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                ReportCard(
                    title = "طلبات اليوم",
                    value = "23",
                    icon = Icons.Default.ShoppingCart,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            item {
                ReportCard(
                    title = "عملاء اليوم",
                    value = "18",
                    icon = Icons.Default.People,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun WeeklyReport() {
    Column {
        Text(
            text = "التقرير الأسبوعي",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Weekly Stats
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ReportCard(
                    title = "مبيعات الأسبوع",
                    value = NumberFormat.getCurrencyInstance(Locale("ar", "SA")).format(8750.0),
                    icon = Icons.Default.AttachMoney,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                ReportCard(
                    title = "طلبات الأسبوع",
                    value = "156",
                    icon = Icons.Default.ShoppingCart,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            item {
                ReportCard(
                    title = "عملاء الأسبوع",
                    value = "89",
                    icon = Icons.Default.People,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun MonthlyReport() {
    Column {
        Text(
            text = "التقرير الشهري",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Monthly Stats
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ReportCard(
                    title = "مبيعات الشهر",
                    value = NumberFormat.getCurrencyInstance(Locale("ar", "SA")).format(35000.0),
                    icon = Icons.Default.AttachMoney,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                ReportCard(
                    title = "طلبات الشهر",
                    value = "624",
                    icon = Icons.Default.ShoppingCart,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            item {
                ReportCard(
                    title = "عملاء الشهر",
                    value = "234",
                    icon = Icons.Default.People,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun ReportCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

enum class ReportType {
    SALES, PRODUCTS, CUSTOMERS, DAILY, WEEKLY, MONTHLY
}

fun getReportTypeText(reportType: ReportType): String {
    return when (reportType) {
        ReportType.SALES -> "المبيعات"
        ReportType.PRODUCTS -> "المنتجات"
        ReportType.CUSTOMERS -> "العملاء"
        ReportType.DAILY -> "يومي"
        ReportType.WEEKLY -> "أسبوعي"
        ReportType.MONTHLY -> "شهري"
    }
}