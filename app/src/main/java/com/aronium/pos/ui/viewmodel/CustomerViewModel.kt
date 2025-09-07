package com.aronium.pos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aronium.pos.data.model.Customer
import com.aronium.pos.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerState(
    val customers: List<Customer> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _customerState = MutableStateFlow(CustomerState())
    val customerState: StateFlow<CustomerState> = _customerState.asStateFlow()

    init {
        loadCustomers()
    }

    fun loadCustomers() {
        viewModelScope.launch {
            _customerState.value = _customerState.value.copy(isLoading = true)
            try {
                customerRepository.getAllCustomers().collect { customers ->
                    _customerState.value = _customerState.value.copy(
                        customers = customers,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _customerState.value = _customerState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun addCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                customerRepository.insertCustomer(customer)
                loadCustomers()
            } catch (e: Exception) {
                _customerState.value = _customerState.value.copy(error = e.message)
            }
        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                customerRepository.updateCustomer(customer)
                loadCustomers()
            } catch (e: Exception) {
                _customerState.value = _customerState.value.copy(error = e.message)
            }
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                customerRepository.deleteCustomer(customer)
                loadCustomers()
            } catch (e: Exception) {
                _customerState.value = _customerState.value.copy(error = e.message)
            }
        }
    }

    fun searchCustomers(query: String) {
        viewModelScope.launch {
            try {
                customerRepository.searchCustomers(query).collect { customers ->
                    _customerState.value = _customerState.value.copy(
                        customers = customers,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _customerState.value = _customerState.value.copy(error = e.message)
            }
        }
    }
}