package com.aronium.pos.ui.screens.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aronium.pos.data.model.Recipe
import com.aronium.pos.data.model.RecipeIngredient
import com.aronium.pos.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesScreen(
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val recipeState by viewModel.recipeState.collectAsState()
    var showAddRecipeDialog by remember { mutableStateOf(false) }
    var showRecipeDetailsDialog by remember { mutableStateOf<Recipe?>(null) }

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
                text = "وصفات الطعام",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { showAddRecipeDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "إضافة وصفة")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = recipeState.searchQuery,
            onValueChange = viewModel::searchRecipes,
            label = { Text("بحث في الوصفات") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recipes List
        if (recipeState.isLoading) {
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
                items(recipeState.recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onViewDetails = { showRecipeDetailsDialog = recipe },
                        onEdit = { /* Navigate to edit recipe */ },
                        onDelete = { viewModel.deleteRecipe(recipe) }
                    )
                }
            }
        }
    }

    // Add Recipe Dialog
    if (showAddRecipeDialog) {
        AddRecipeDialog(
            onDismiss = { showAddRecipeDialog = false },
            onAddRecipe = { recipe, ingredients ->
                viewModel.addRecipe(recipe, ingredients)
                showAddRecipeDialog = false
            }
        )
    }

    // Recipe Details Dialog
    showRecipeDetailsDialog?.let { recipe ->
        RecipeDetailsDialog(
            recipe = recipe,
            ingredients = recipeState.currentRecipeIngredients,
            onDismiss = { showRecipeDetailsDialog = null },
            onLoadIngredients = { viewModel.loadRecipeIngredients(recipe.id) }
        )
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onViewDetails: () -> Unit,
    onEdit: () -> Unit,
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
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                Row {
                    IconButton(onClick = onViewDetails) {
                        Icon(Icons.Default.Visibility, contentDescription = "عرض التفاصيل")
                    }
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "تعديل")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Recipe Info
            recipe.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Time and Servings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "التحضير: ${recipe.prepTimeMinutes} دقيقة",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "الطهي: ${recipe.cookTimeMinutes} دقيقة",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.People,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${recipe.servings} حصة",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun AddRecipeDialog(
    onDismiss: () -> Unit,
    onAddRecipe: (Recipe, List<RecipeIngredient>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var prepTime by remember { mutableStateOf("") }
    var cookTime by remember { mutableStateOf("") }
    var servings by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(mutableListOf<RecipeIngredient>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة وصفة جديدة") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم الوصفة") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("الوصف") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = prepTime,
                        onValueChange = { prepTime = it },
                        label = { Text("وقت التحضير (دقيقة)") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = cookTime,
                        onValueChange = { cookTime = it },
                        label = { Text("وقت الطهي (دقيقة)") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                OutlinedTextField(
                    value = servings,
                    onValueChange = { servings = it },
                    label = { Text("عدد الحصص") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("طريقة التحضير") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                
                // Ingredients Section
                Text(
                    text = "المكونات:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                ingredients.forEachIndexed { index, ingredient ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ingredient.name,
                            onValueChange = { 
                                ingredients[index] = ingredient.copy(name = it)
                            },
                            label = { Text("اسم المكون") },
                            modifier = Modifier.weight(2f)
                        )
                        
                        OutlinedTextField(
                            value = ingredient.quantity.toString(),
                            onValueChange = { 
                                ingredients[index] = ingredient.copy(quantity = it.toDoubleOrNull() ?: 0.0)
                            },
                            label = { Text("الكمية") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        OutlinedTextField(
                            value = ingredient.unit,
                            onValueChange = { 
                                ingredients[index] = ingredient.copy(unit = it)
                            },
                            label = { Text("الوحدة") },
                            modifier = Modifier.weight(1f)
                        )
                        
                        IconButton(
                            onClick = { ingredients.removeAt(index) }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "حذف")
                        }
                    }
                }
                
                Button(
                    onClick = {
                        ingredients.add(RecipeIngredient(
                            recipeId = 0,
                            name = "",
                            quantity = 0.0,
                            unit = ""
                        ))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("إضافة مكون")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && instructions.isNotBlank()) {
                        val recipe = Recipe(
                            name = name,
                            description = description.takeIf { it.isNotBlank() },
                            prepTimeMinutes = prepTime.toIntOrNull() ?: 0,
                            cookTimeMinutes = cookTime.toIntOrNull() ?: 0,
                            servings = servings.toIntOrNull() ?: 1,
                            instructions = instructions
                        )
                        val validIngredients = ingredients.filter { it.name.isNotBlank() }
                        onAddRecipe(recipe, validIngredients)
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

@Composable
fun RecipeDetailsDialog(
    recipe: Recipe,
    ingredients: List<RecipeIngredient>,
    onDismiss: () -> Unit,
    onLoadIngredients: () -> Unit
) {
    LaunchedEffect(recipe.id) {
        onLoadIngredients()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(recipe.name) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                recipe.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                // Time and Servings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("وقت التحضير: ${recipe.prepTimeMinutes} دقيقة")
                    Text("وقت الطهي: ${recipe.cookTimeMinutes} دقيقة")
                    Text("الحصص: ${recipe.servings}")
                }
                
                // Instructions
                Text(
                    text = "طريقة التحضير:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = recipe.instructions,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                // Ingredients
                Text(
                    text = "المكونات:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                ingredients.forEach { ingredient ->
                    Text(
                        text = "• ${ingredient.name}: ${ingredient.quantity} ${ingredient.unit}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("موافق")
            }
        }
    )
}