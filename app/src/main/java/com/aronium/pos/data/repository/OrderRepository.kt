package com.aronium.pos.data.repository

import com.aronium.pos.data.dao.OrderDao
import com.aronium.pos.data.dao.OrderItemDao
import com.aronium.pos.data.model.Order
import com.aronium.pos.data.model.OrderItem
import com.aronium.pos.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {
    fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrders()

    suspend fun getOrderById(id: Long): Order? = orderDao.getOrderById(id)

    fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>> = 
        orderDao.getOrdersByStatus(status)

    fun getOrdersByCustomer(customerId: Long): Flow<List<Order>> = 
        orderDao.getOrdersByCustomer(customerId)

    fun getTodayOrders(): Flow<List<Order>> = orderDao.getTodayOrders()

    fun getOrdersByDateRange(startTime: Long, endTime: Long): Flow<List<Order>> = 
        orderDao.getOrdersByDateRange(startTime, endTime)

    suspend fun insertOrder(order: Order): Long = orderDao.insertOrder(order)

    suspend fun updateOrder(order: Order) = orderDao.updateOrder(order)

    suspend fun deleteOrder(order: Order) = orderDao.deleteOrder(order)

    suspend fun deleteOrderById(id: Long) = orderDao.deleteOrderById(id)

    suspend fun getOrderCountByStatus(status: OrderStatus): Int = 
        orderDao.getOrderCountByStatus(status)

    suspend fun getTodayRevenueByStatus(status: OrderStatus): Double? = 
        orderDao.getTodayRevenueByStatus(status)

    // Order Items
    fun getOrderItemsByOrderId(orderId: Long): Flow<List<OrderItem>> = 
        orderItemDao.getOrderItemsByOrderId(orderId)

    suspend fun insertOrderItem(orderItem: OrderItem): Long = 
        orderItemDao.insertOrderItem(orderItem)

    suspend fun insertOrderItems(orderItems: List<OrderItem>) = 
        orderItemDao.insertOrderItems(orderItems)

    suspend fun updateOrderItem(orderItem: OrderItem) = 
        orderItemDao.updateOrderItem(orderItem)

    suspend fun deleteOrderItem(orderItem: OrderItem) = 
        orderItemDao.deleteOrderItem(orderItem)

    suspend fun deleteOrderItemsByOrderId(orderId: Long) = 
        orderItemDao.deleteOrderItemsByOrderId(orderId)
}