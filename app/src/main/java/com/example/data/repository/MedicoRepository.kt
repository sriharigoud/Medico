package com.example.data.repository

import com.example.R
import com.example.data.local.AddressEntity
import com.example.data.local.MedicoDao
import com.example.data.local.OrderEntity
import com.example.data.local.PrescriptionEntity
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicoRepository(private val dao: MedicoDao) {

    val addresses: Flow<List<DeliveryAddress>> = dao.getAllAddresses().map { entities ->
        if (entities.isEmpty()) {
            getDefaultAddresses()
        } else {
            entities.map {
                DeliveryAddress(
                    id = it.id,
                    title = it.title,
                    street = it.street,
                    cityStateZip = it.cityStateZip,
                    country = it.country,
                    tag = it.tag,
                    isPrimary = it.isPrimary
                )
            }
        }
    }

    val prescriptions: Flow<List<PrescriptionItem>> = dao.getAllPrescriptions().map { entities ->
        if (entities.isEmpty()) {
            getDefaultPrescriptions()
        } else {
            entities.map {
                PrescriptionItem(
                    id = it.id,
                    title = it.title,
                    uploadDate = it.uploadDate,
                    status = try {
                        PrescriptionStatus.valueOf(it.status)
                    } catch (e: Exception) {
                        PrescriptionStatus.PENDING
                    },
                    expiryText = it.expiryText,
                    thumbnailRes = R.drawable.img_prescription_sample_1785001665470,
                    doctorName = it.doctorName,
                    patientName = it.patientName
                )
            }
        }
    }

    suspend fun saveAddress(address: DeliveryAddress) {
        dao.insertAddress(
            AddressEntity(
                id = address.id,
                title = address.title,
                street = address.street,
                cityStateZip = address.cityStateZip,
                country = address.country,
                tag = address.tag,
                isPrimary = address.isPrimary
            )
        )
    }

    suspend fun deleteAddress(id: String) {
        dao.deleteAddress(id)
    }

    suspend fun savePrescription(prescription: PrescriptionItem) {
        dao.insertPrescription(
            PrescriptionEntity(
                id = prescription.id,
                title = prescription.title,
                uploadDate = prescription.uploadDate,
                status = prescription.status.name,
                expiryText = prescription.expiryText,
                doctorName = prescription.doctorName,
                patientName = prescription.patientName
            )
        )
    }

    suspend fun deletePrescription(id: String) {
        dao.deletePrescription(id)
    }

    fun getDefaultPharmacyQuotes(): List<PharmacyQuote> = listOf(
        PharmacyQuote(
            id = "1",
            pharmacyName = "HealthWay Pharmacy",
            rating = 4.8,
            distanceKm = 1.2,
            quotedPrice = 42.50,
            badgeType = "In Stock",
            estimatedArrival = "Today, 4:00 PM"
        ),
        PharmacyQuote(
            id = "2",
            pharmacyName = "City Meds Depot",
            rating = 4.5,
            distanceKm = 0.8,
            quotedPrice = 48.00,
            badgeType = "Nearest",
            estimatedArrival = "Ready in 30m"
        ),
        PharmacyQuote(
            id = "3",
            pharmacyName = "CareFirst Apothecary",
            rating = 4.9,
            distanceKm = 3.5,
            quotedPrice = 39.99,
            badgeType = "Best Choice",
            estimatedArrival = "Tomorrow, 10:00 AM",
            isLowStock = true
        )
    )

    fun getDefaultOrderItems(): List<OrderItem> = listOf(
        OrderItem(
            id = "item_1",
            name = "Amoxicillin 500mg",
            quantityDetails = "21 Capsules",
            packType = "Course Pack",
            price = 12.50,
            imageRes = R.drawable.img_medicine_sample_1785001645525
        ),
        OrderItem(
            id = "item_2",
            name = "Paracetamol 500mg",
            quantityDetails = "20 Tablets",
            packType = "Extra Strength",
            price = 4.20,
            imageRes = R.drawable.img_medicine_sample_1785001645525
        )
    )

    fun getDefaultAddresses(): List<DeliveryAddress> = listOf(
        DeliveryAddress(
            id = "addr_1",
            title = "Primary Residence",
            street = "4522 Oakwood Avenue, Suite 201",
            cityStateZip = "Los Angeles, CA 90004",
            tag = "Home",
            isPrimary = true
        ),
        DeliveryAddress(
            id = "addr_2",
            title = "Medical Center Office",
            street = "789 Health Plaza, West Wing\nFloor 4, Unit 402",
            cityStateZip = "San Francisco, CA 94103",
            tag = "Work",
            isPrimary = false
        ),
        DeliveryAddress(
            id = "addr_3",
            title = "Parent's Home",
            street = "1221 Sunrise Blvd\nBuilding C, Apt 12",
            cityStateZip = "Miami, FL 33101",
            tag = "Other",
            isPrimary = false
        )
    )

    fun getDefaultPaymentCards(): List<PaymentCard> = listOf(
        PaymentCard(
            id = "card_1",
            cardBrand = "VISA",
            last4 = "4291",
            expiry = "09/27",
            holderName = "ALEX R. JOHNSON",
            isPrimary = true
        ),
        PaymentCard(
            id = "card_2",
            cardBrand = "MASTERCARD",
            last4 = "8812",
            expiry = "11/28",
            holderName = "ALEX R. JOHNSON",
            isPrimary = false
        )
    )

    fun getDefaultUpiAccounts(): List<UpiAccount> = listOf(
        UpiAccount("upi_1", "alex.johnson@okaxis", "Default UPI ID", isDefault = true),
        UpiAccount("upi_2", "9876543210@paytm", "Personal UPI", isDefault = false)
    )

    fun getDefaultPrescriptions(): List<PrescriptionItem> = listOf(
        PrescriptionItem(
            id = "presc_1",
            title = "General Wellness Check",
            uploadDate = "Oct 24, 2023",
            status = PrescriptionStatus.VERIFIED,
            expiryText = "Expires in 6 months",
            thumbnailRes = R.drawable.img_prescription_sample_1785001665470
        ),
        PrescriptionItem(
            id = "presc_2",
            title = "Cardiology Follow-up",
            uploadDate = "Today, 10:15 AM",
            status = PrescriptionStatus.PENDING,
            expiryText = "Verification in progress",
            thumbnailRes = R.drawable.img_prescription_sample_1785001665470
        ),
        PrescriptionItem(
            id = "presc_3",
            title = "Annual Eye Exam",
            uploadDate = "Jan 12, 2023",
            status = PrescriptionStatus.EXPIRED,
            expiryText = "Expired Oct 2023",
            thumbnailRes = R.drawable.img_prescription_sample_1785001665470
        )
    )

    fun getInitialActiveOrder(): ActiveOrder = ActiveOrder(
        orderId = "MED-882190",
        orderNumber = "ORD-9026",
        pharmacyName = "City Health Central Pharmacy",
        statusText = "Courier is nearby",
        etaMinutes = 8,
        courierName = "Mark J.",
        courierRating = 4.9,
        courierRole = "Medico Express Partner",
        courierAvatarRes = R.drawable.img_courier_partner_1785001631588,
        deliveryAddress = "123 Health Ave, Clinical District, SW1 2PT",
        paymentMethod = "Visa ending in •••• 4242",
        items = listOf(
            OrderItem("1", "Paracetamol 500mg", "x2", "Tablets", 12.00),
            OrderItem("2", "Amoxicillin Syrup", "x1", "100ml bottle", 45.50),
            OrderItem("3", "Vitamin C 1000mg", "x1", "Effervescent", 18.20)
        ),
        medicineTotal = 75.70,
        deliveryFee = 5.00,
        taxGst = 3.20,
        grandTotal = 83.90,
        trackingSteps = listOf(
            TrackingStep("Order Verified", "Pharmacist reviewed your prescription at 09:15 AM", "09:15 AM", DeliveryStepStatus.COMPLETED),
            TrackingStep("Pharmacist packed your order", "Packing completed at City Center Pharmacy at 10:02 AM", "10:02 AM", DeliveryStepStatus.COMPLETED),
            TrackingStep("Courier is nearby", "Mark is 0.8 miles away from your location", "10:24 AM", DeliveryStepStatus.ACTIVE),
            TrackingStep("Delivered", "Awaiting drop-off at your door", "--", DeliveryStepStatus.PENDING)
        )
    )

    fun getDefaultPastOrders(): List<PastOrder> = listOf(
        PastOrder(
            orderId = "MED-771920",
            orderNumber = "ORD-8812",
            pharmacyName = "HealthWay Pharmacy",
            dateText = "July 12, 2026 • 2:15 PM",
            statusText = "DELIVERED",
            itemsSummary = "Amoxicillin 500mg (21 Caps), Vitamin C 1000mg",
            totalAmount = 48.50,
            isDelivered = true
        ),
        PastOrder(
            orderId = "MED-660412",
            orderNumber = "ORD-7540",
            pharmacyName = "CareFirst Apothecary",
            dateText = "June 28, 2026 • 11:30 AM",
            statusText = "DELIVERED",
            itemsSummary = "Dermatology Cream 50g, Anti-allergy Tabs",
            totalAmount = 39.99,
            isDelivered = true
        ),
        PastOrder(
            orderId = "MED-550110",
            orderNumber = "ORD-6120",
            pharmacyName = "City Meds Depot",
            dateText = "May 14, 2026 • 09:45 AM",
            statusText = "DELIVERED",
            itemsSummary = "Paracetamol 500mg (20 Tabs), Cough Syrup",
            totalAmount = 24.80,
            isDelivered = true
        )
    )
}

