package com.aronium.pos.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.aronium.pos.data.model.*
import com.aronium.pos.data.dao.*

@Database(
    entities = [
        Product::class,
        Order::class,
        OrderItem::class,
        Customer::class,
        Recipe::class,
        RecipeIngredient::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AroniumDatabase : RoomDatabase() {
    
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun customerDao(): CustomerDao
    abstract fun recipeDao(): RecipeDao
    abstract fun recipeIngredientDao(): RecipeIngredientDao

    companion object {
        @Volatile
        private var INSTANCE: AroniumDatabase? = null

        fun getDatabase(context: Context): AroniumDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AroniumDatabase::class.java,
                    "aronium_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}