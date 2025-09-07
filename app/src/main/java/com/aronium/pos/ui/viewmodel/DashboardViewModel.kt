package com.aronium.pos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aronium.pos.data.model.OrderStatus
import com.aronium.pos.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                val todayOrders = orderRepository.getTodayOrders()
                val pendingCount = orderRepository.getOrderCountByStatus(OrderStatus.PENDING)
                val preparingCount = orderRepository.getOrderCountByStatus(OrderStatus.PREPARING)
                val readyCount = orderRepository.getOrderCountByStatus(OrderStatus.READY)
                val completedCount = orderRepository.getOrderCountByStatus(OrderStatus.COMPLETED)
                
                val todayRevenue = orderRepository.getTodayRevenueByStatus(OrderStatus.COMPLETED) ?: 0.0
                val totalOrders = pendingCount + preparingCount + readyCount + completedCount
                val activeOrders = pendingCount + preparingCount + readyCount

                _dashboardState.value = _dashboardState.value.copy(
                    todayRevenue = todayRevenue,
                    totalOrders = totalOrders,
                    activeOrders = activeOrders,
                    isLoading = false
                )
            } catch (e: Exception) {
                _dashboardState.value = _dashboardState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun refreshData() {
        _dashboardState.value = _dashboardState.value.copy(isLoading = true, error = null)
        loadDashboardData()
    }
}

data class DashboardState(
    val todayRevenue: Double = 0.0,
    val totalOrders: Int = 0,
    val activeOrders: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)