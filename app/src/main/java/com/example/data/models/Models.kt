package com.example.data.models

import androidx.annotation.DrawableRes

enum class BottomTab {
    HOME, ORDERS, PRESCRIPTIONS, PROFILE
}

data class DeliveryAddress(
    val id: String,
    val title: String,
    val street: String,
    val cityStateZip: String,
    val country: String = "United States",
    val tag: String, // "Home", "Work", "Other"
    val isPrimary: Boolean = false
)

data class PharmacyQuote(
    val id: String,
    val pharmacyName: String,
    val rating: Double,
    val distanceKm: Double,
    val quotedPrice: Double,
    val badgeType: String, // "In Stock", "Nearest", "Best Choice"
    val estimatedArrival: String,
    val isLowStock: Boolean = false,
    val isSecurePaymentRequired: Boolean = true
)

data class OrderItem(
    val id: String,
    val name: String,
    val quantityDetails: String,
    val packType: String,
    val price: Double,
    @DrawableRes val imageRes: Int? = null
)

data class PaymentCard(
    val id: String,
    val cardBrand: String, // "VISA", "MASTERCARD"
    val last4: String,
    val expiry: String,
    val holderName: String,
    val isPrimary: Boolean = false
)

data class UpiAccount(
    val id: String,
    val upiId: String,
    val label: String,
    val isDefault: Boolean = false
)

enum class PrescriptionStatus {
    VERIFIED, PENDING, EXPIRED
}

data class PrescriptionItem(
    val id: String,
    val title: String,
    val uploadDate: String,
    val status: PrescriptionStatus,
    val expiryText: String,
    @DrawableRes val thumbnailRes: Int? = null,
    val doctorName: String = "Dr. C. Frank",
    val patientName: String = "Christopher Henderson"
)

enum class DeliveryStepStatus {
    COMPLETED, ACTIVE, PENDING
}

data class TrackingStep(
    val title: String,
    val description: String,
    val timestamp: String,
    val status: DeliveryStepStatus
)

data class ActiveOrder(
    val orderId: String,
    val orderNumber: String,
    val pharmacyName: String,
    val statusText: String, // "Store Packing", "Pickup Boy Assigned", "On the Way", "Delivered"
    val etaMinutes: Int,
    val courierName: String,
    val courierRating: Double,
    val courierRole: String,
    @DrawableRes val courierAvatarRes: Int? = null,
    val deliveryAddress: String,
    val paymentMethod: String,
    val items: List<OrderItem>,
    val medicineTotal: Double,
    val deliveryFee: Double,
    val taxGst: Double,
    val grandTotal: Double,
    val trackingSteps: List<TrackingStep>
)

data class PastOrder(
    val orderId: String,
    val orderNumber: String,
    val pharmacyName: String,
    val dateText: String,
    val statusText: String = "DELIVERED",
    val itemsSummary: String,
    val totalAmount: Double,
    val isDelivered: Boolean = true
)

