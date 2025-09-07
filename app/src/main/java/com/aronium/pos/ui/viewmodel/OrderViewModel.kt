package com.aronium.pos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aronium.pos.data.model.Order
import com.aronium.pos.data.model.OrderItem
import com.aronium.pos.data.model.OrderStatus
import com.aronium.pos.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orderState = MutableStateFlow(OrderState())
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            orderRepository.getAllOrders().collect { orders ->
                _orderState.value = _orderState.value.copy(
                    orders = orders,
                    isLoading = false
                )
            }
        }
    }

    fun loadOrdersByStatus(status: OrderStatus?) {
        if (status == null) {
            loadOrders()
        } else {
            viewModelScope.launch {
                orderRepository.getOrdersByStatus(status).collect { orders ->
                    _orderState.value = _orderState.value.copy(
                        orders = orders,
                        selectedStatus = status
                    )
                }
            }
        }
    }

    fun loadTodayOrders() {
        viewModelScope.launch {
            orderRepository.getTodayOrders().collect { orders ->
                _orderState.value = _orderState.value.copy(
                    orders = orders,
                    isTodayFilter = true
                )
            }
        }
    }

    fun addOrder(order: Order, orderItems: List<OrderItem>) {
        viewModelScope.launch {
            try {
                val orderId = orderRepository.insertOrder(order)
                val itemsWithOrderId = orderItems.map { it.copy(orderId = orderId) }
                orderRepository.insertOrderItems(itemsWithOrderId)
                _orderState.value = _orderState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _orderState.value = _orderState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun updateOrderStatus(order: Order, newStatus: OrderStatus) {
        viewModelScope.launch {
            try {
                val updatedOrder = order.copy(
                    status = newStatus,
                    updatedAt = System.currentTimeMillis()
                )
                orderRepository.updateOrder(updatedOrder)
                _orderState.value = _orderState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _orderState.value = _orderState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            try {
                orderRepository.deleteOrderItemsByOrderId(order.id)
                orderRepository.deleteOrder(order)
                _orderState.value = _orderState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _orderState.value = _orderState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _orderState.value = _orderState.value.copy(error = null)
    }
}

data class OrderState(
    val orders: List<Order> = emptyList(),
    val selectedStatus: OrderStatus? = null,
    val isTodayFilter: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)