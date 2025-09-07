package com.aronium.pos.data.repository

import com.aronium.pos.data.dao.RecipeDao
import com.aronium.pos.data.dao.RecipeIngredientDao
import com.aronium.pos.data.model.Recipe
import com.aronium.pos.data.model.RecipeIngredient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeIngredientDao: RecipeIngredientDao
) {
    fun getAllRecipes(): Flow<List<Recipe>> = recipeDao.getAllRecipes()

    suspend fun getRecipeById(id: Long): Recipe? = recipeDao.getRecipeById(id)

    fun searchRecipes(searchQuery: String): Flow<List<Recipe>> = 
        recipeDao.searchRecipes(searchQuery)

    suspend fun insertRecipe(recipe: Recipe): Long = recipeDao.insertRecipe(recipe)

    suspend fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)

    suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)

    suspend fun deleteRecipeById(id: Long) = recipeDao.deleteRecipeById(id)

    // Recipe Ingredients
    fun getIngredientsByRecipeId(recipeId: Long): Flow<List<RecipeIngredient>> = 
        recipeIngredientDao.getIngredientsByRecipeId(recipeId)

    suspend fun insertIngredient(ingredient: RecipeIngredient): Long = 
        recipeIngredientDao.insertIngredient(ingredient)

    suspend fun insertIngredients(ingredients: List<RecipeIngredient>) = 
        recipeIngredientDao.insertIngredients(ingredients)

    suspend fun updateIngredient(ingredient: RecipeIngredient) = 
        recipeIngredientDao.updateIngredient(ingredient)

    suspend fun deleteIngredient(ingredient: RecipeIngredient) = 
        recipeIngredientDao.deleteIngredient(ingredient)

    suspend fun deleteIngredientsByRecipeId(recipeId: Long) = 
        recipeIngredientDao.deleteIngredientsByRecipeId(recipeId)
}