package com.aronium.pos.data.repository

import com.aronium.pos.data.dao.CustomerDao
import com.aronium.pos.data.model.Customer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
) {
    fun getAllCustomers(): Flow<List<Customer>> = customerDao.getAllCustomers()

    suspend fun getCustomerById(id: Long): Customer? = customerDao.getCustomerById(id)

    fun searchCustomers(searchQuery: String): Flow<List<Customer>> = 
        customerDao.searchCustomers(searchQuery)

    fun searchCustomersByPhone(phone: String): Flow<List<Customer>> = 
        customerDao.searchCustomersByPhone(phone)

    suspend fun insertCustomer(customer: Customer): Long = customerDao.insertCustomer(customer)

    suspend fun updateCustomer(customer: Customer) = customerDao.updateCustomer(customer)

    suspend fun deleteCustomer(customer: Customer) = customerDao.deleteCustomer(customer)

    suspend fun deleteCustomerById(id: Long) = customerDao.deleteCustomerById(id)
}