package com.aronium.pos.data.dao

import androidx.room.*
import com.aronium.pos.data.model.RecipeIngredient
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeIngredientDao {
    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId ORDER BY name ASC")
    fun getIngredientsByRecipeId(recipeId: Long): Flow<List<RecipeIngredient>>

    @Query("SELECT * FROM recipe_ingredients WHERE id = :id")
    suspend fun getIngredientById(id: Long): RecipeIngredient?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: RecipeIngredient): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<RecipeIngredient>)

    @Update
    suspend fun updateIngredient(ingredient: RecipeIngredient)

    @Delete
    suspend fun deleteIngredient(ingredient: RecipeIngredient)

    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsByRecipeId(recipeId: Long)

    @Query("DELETE FROM recipe_ingredients WHERE id = :id")
    suspend fun deleteIngredientById(id: Long)
}