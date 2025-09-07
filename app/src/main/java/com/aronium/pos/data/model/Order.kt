package com.aronium.pos.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "orders")
@Parcelize
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val customerId: Long? = null,
    val status: OrderStatus,
    val totalAmount: Double,
    val taxAmount: Double = 0.0,
    val discountAmount: Double = 0.0,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
enum class OrderStatus : Parcelable {
    PENDING,
    PREPARING,
    READY,
    COMPLETED,
    CANCELLED
}