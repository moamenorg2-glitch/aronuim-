package com.aronium.pos.di

import android.content.Context
import com.aronium.pos.data.dao.*
import com.aronium.pos.data.database.AroniumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AroniumDatabase {
        return AroniumDatabase.getDatabase(context)
    }

    @Provides
    fun provideProductDao(database: AroniumDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideOrderDao(database: AroniumDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    fun provideOrderItemDao(database: AroniumDatabase): OrderItemDao {
        return database.orderItemDao()
    }

    @Provides
    fun provideCustomerDao(database: AroniumDatabase): CustomerDao {
        return database.customerDao()
    }

    @Provides
    fun provideRecipeDao(database: AroniumDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    fun provideRecipeIngredientDao(database: AroniumDatabase): RecipeIngredientDao {
        return database.recipeIngredientDao()
    }
}