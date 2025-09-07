package com.aronium.pos.data.repository

import com.aronium.pos.data.dao.ProductDao
import com.aronium.pos.data.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()

    suspend fun getProductById(id: Long): Product? = productDao.getProductById(id)

    fun getProductsByCategory(category: String): Flow<List<Product>> = 
        productDao.getProductsByCategory(category)

    fun searchProducts(searchQuery: String): Flow<List<Product>> = 
        productDao.searchProducts(searchQuery)

    fun getAvailableProducts(): Flow<List<Product>> = productDao.getAvailableProducts()

    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)

    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)

    suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)

    suspend fun deleteProductById(id: Long) = productDao.deleteProductById(id)

    fun getAllCategories(): Flow<List<String>> = productDao.getAllCategories()
}