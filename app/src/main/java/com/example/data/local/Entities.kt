package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey val id: String,
    val title: String,
    val street: String,
    val cityStateZip: String,
    val country: String,
    val tag: String,
    val isPrimary: Boolean
)

@Entity(tableName = "prescriptions")
data class PrescriptionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val uploadDate: String,
    val status: String,
    val expiryText: String,
    val doctorName: String,
    val patientName: String
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val orderNumber: String,
    val pharmacyName: String,
    val statusText: String,
    val etaMinutes: Int,
    val grandTotal: Double,
    val timestamp: Long = System.currentTimeMillis()
)
