package com.aronium.pos.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "الإعدادات",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Settings Categories
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getSettingsCategories()) { category ->
                SettingsCategoryCard(category = category)
            }
        }
    }
}

@Composable
fun SettingsCategoryCard(category: SettingsCategory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class SettingsCategory(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

fun getSettingsCategories(): List<SettingsCategory> {
    return listOf(
        SettingsCategory(
            title = "الإعدادات العامة",
            description = "إعدادات التطبيق الأساسية",
            icon = Icons.Default.Settings,
            onClick = { /* Navigate to general settings */ }
        ),
        SettingsCategory(
            title = "إعدادات الطابعة",
            description = "تكوين الطابعة والفواتير",
            icon = Icons.Default.Print,
            onClick = { /* Navigate to printer settings */ }
        ),
        SettingsCategory(
            title = "إعدادات الضرائب",
            description = "تكوين الضرائب والرسوم",
            icon = Icons.Default.Calculate,
            onClick = { /* Navigate to tax settings */ }
        ),
        SettingsCategory(
            title = "إعدادات العملة",
            description = "تكوين العملة والعرض",
            icon = Icons.Default.AttachMoney,
            onClick = { /* Navigate to currency settings */ }
        ),
        SettingsCategory(
            title = "إعدادات اللغة",
            description = "تغيير لغة التطبيق",
            icon = Icons.Default.Language,
            onClick = { /* Navigate to language settings */ }
        ),
        SettingsCategory(
            title = "النسخ الاحتياطي",
            description = "إدارة النسخ الاحتياطية",
            icon = Icons.Default.Backup,
            onClick = { /* Navigate to backup settings */ }
        ),
        SettingsCategory(
            title = "حول التطبيق",
            description = "معلومات التطبيق والإصدار",
            icon = Icons.Default.Info,
            onClick = { /* Navigate to about */ }
        )
    )
}