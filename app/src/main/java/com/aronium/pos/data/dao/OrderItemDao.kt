package com.aronium.pos.data.dao

import androidx.room.*
import com.aronium.pos.data.model.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDao {
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsByOrderId(orderId: Long): Flow<List<OrderItem>>

    @Query("SELECT * FROM order_items WHERE id = :id")
    suspend fun getOrderItemById(id: Long): OrderItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItem>)

    @Update
    suspend fun updateOrderItem(orderItem: OrderItem)

    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem)

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItemsByOrderId(orderId: Long)

    @Query("DELETE FROM order_items WHERE id = :id")
    suspend fun deleteOrderItemById(id: Long)
}