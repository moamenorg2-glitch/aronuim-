package com.aronium.pos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aronium.pos.data.model.Recipe
import com.aronium.pos.data.model.RecipeIngredient
import com.aronium.pos.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipeState = MutableStateFlow(RecipeState())
    val recipeState: StateFlow<RecipeState> = _recipeState.asStateFlow()

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            recipeRepository.getAllRecipes().collect { recipes ->
                _recipeState.value = _recipeState.value.copy(
                    recipes = recipes,
                    isLoading = false
                )
            }
        }
    }

    fun searchRecipes(query: String) {
        if (query.isBlank()) {
            loadRecipes()
        } else {
            viewModelScope.launch {
                recipeRepository.searchRecipes(query).collect { recipes ->
                    _recipeState.value = _recipeState.value.copy(
                        recipes = recipes,
                        searchQuery = query
                    )
                }
            }
        }
    }

    fun loadRecipeIngredients(recipeId: Long) {
        viewModelScope.launch {
            recipeRepository.getIngredientsByRecipeId(recipeId).collect { ingredients ->
                _recipeState.value = _recipeState.value.copy(
                    currentRecipeIngredients = ingredients
                )
            }
        }
    }

    fun addRecipe(recipe: Recipe, ingredients: List<RecipeIngredient>) {
        viewModelScope.launch {
            try {
                val recipeId = recipeRepository.insertRecipe(recipe)
                val ingredientsWithRecipeId = ingredients.map { it.copy(recipeId = recipeId) }
                recipeRepository.insertIngredients(ingredientsWithRecipeId)
                _recipeState.value = _recipeState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _recipeState.value = _recipeState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun updateRecipe(recipe: Recipe, ingredients: List<RecipeIngredient>) {
        viewModelScope.launch {
            try {
                recipeRepository.updateRecipe(recipe)
                recipeRepository.deleteIngredientsByRecipeId(recipe.id)
                val ingredientsWithRecipeId = ingredients.map { it.copy(recipeId = recipe.id) }
                recipeRepository.insertIngredients(ingredientsWithRecipeId)
                _recipeState.value = _recipeState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _recipeState.value = _recipeState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                recipeRepository.deleteIngredientsByRecipeId(recipe.id)
                recipeRepository.deleteRecipe(recipe)
                _recipeState.value = _recipeState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _recipeState.value = _recipeState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _recipeState.value = _recipeState.value.copy(error = null)
    }
}

data class RecipeState(
    val recipes: List<Recipe> = emptyList(),
    val currentRecipeIngredients: List<RecipeIngredient> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)