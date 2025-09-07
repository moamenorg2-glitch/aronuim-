package com.aronium.pos.data.dao

import androidx.room.*
import com.aronium.pos.data.model.Order
import com.aronium.pos.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: Long): Order?

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY createdAt DESC")
    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getOrdersByCustomer(customerId: Long): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE DATE(createdAt/1000, 'unixepoch') = DATE('now') ORDER BY createdAt DESC")
    fun getTodayOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE createdAt BETWEEN :startTime AND :endTime ORDER BY createdAt DESC")
    fun getOrdersByDateRange(startTime: Long, endTime: Long): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteOrderById(id: Long)

    @Query("SELECT COUNT(*) FROM orders WHERE status = :status")
    suspend fun getOrderCountByStatus(status: OrderStatus): Int

    @Query("SELECT SUM(totalAmount) FROM orders WHERE status = :status AND DATE(createdAt/1000, 'unixepoch') = DATE('now')")
    suspend fun getTodayRevenueByStatus(status: OrderStatus): Double?
}