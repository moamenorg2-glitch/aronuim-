package com.aronium.pos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aronium.pos.data.model.Product
import com.aronium.pos.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productState = MutableStateFlow(ProductState())
    val productState: StateFlow<ProductState> = _productState.asStateFlow()

    init {
        loadProducts()
        loadCategories()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts().collect { products ->
                _productState.value = _productState.value.copy(
                    products = products,
                    isLoading = false
                )
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            productRepository.getAllCategories().collect { categories ->
                _productState.value = _productState.value.copy(
                    categories = categories
                )
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            loadProducts()
        } else {
            viewModelScope.launch {
                productRepository.searchProducts(query).collect { products ->
                    _productState.value = _productState.value.copy(
                        products = products,
                        searchQuery = query
                    )
                }
            }
        }
    }

    fun filterByCategory(category: String?) {
        if (category == null) {
            loadProducts()
        } else {
            viewModelScope.launch {
                productRepository.getProductsByCategory(category).collect { products ->
                    _productState.value = _productState.value.copy(
                        products = products,
                        selectedCategory = category
                    )
                }
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.insertProduct(product)
                _productState.value = _productState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _productState.value = _productState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.updateProduct(product)
                _productState.value = _productState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _productState.value = _productState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.deleteProduct(product)
                _productState.value = _productState.value.copy(
                    error = null
                )
            } catch (e: Exception) {
                _productState.value = _productState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _productState.value = _productState.value.copy(error = null)
    }
}

data class ProductState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)