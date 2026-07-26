package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MedicoDatabase
import com.example.data.models.*
import com.example.data.repository.MedicoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MedicoViewModel(application: Application) : AndroidViewModel(application) {

    private val db = MedicoDatabase.getDatabase(application)
    private val repository = MedicoRepository(db.medicoDao())

    val addresses: StateFlow<List<DeliveryAddress>> = repository.addresses.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        repository.getDefaultAddresses()
    )

    private val _inMemoryPrescriptions = MutableStateFlow<List<PrescriptionItem>?>(null)
    val prescriptions: StateFlow<List<PrescriptionItem>> = combine(
        repository.prescriptions,
        _inMemoryPrescriptions
    ) { repoItems, memoryItems ->
        memoryItems ?: repoItems
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        repository.getDefaultPrescriptions()
    )

    private val _currentTab = MutableStateFlow(BottomTab.HOME)
    val currentTab: StateFlow<BottomTab> = _currentTab.asStateFlow()

    private val _currentScreen = MutableStateFlow("SET_LOCATION")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _selectedAddress = MutableStateFlow(
        DeliveryAddress(
            id = "sel_1",
            title = "Market Street 101",
            street = "101 Market Street, Financial District",
            cityStateZip = "San Francisco, CA 94105",
            tag = "Home",
            isPrimary = true
        )
    )
    val selectedAddress: StateFlow<DeliveryAddress> = _selectedAddress.asStateFlow()

    private val _selectedPrescription = MutableStateFlow<PrescriptionItem?>(null)
    val selectedPrescription: StateFlow<PrescriptionItem?> = _selectedPrescription.asStateFlow()

    private val _quotes = MutableStateFlow(repository.getDefaultPharmacyQuotes())
    val quotes: StateFlow<List<PharmacyQuote>> = _quotes.asStateFlow()

    private val _selectedQuote = MutableStateFlow<PharmacyQuote?>(repository.getDefaultPharmacyQuotes().first())
    val selectedQuote: StateFlow<PharmacyQuote?> = _selectedQuote.asStateFlow()

    private val _orderItems = MutableStateFlow(repository.getDefaultOrderItems())
    val orderItems: StateFlow<List<OrderItem>> = _orderItems.asStateFlow()

    private val _paymentCards = MutableStateFlow(repository.getDefaultPaymentCards())
    val paymentCards: StateFlow<List<PaymentCard>> = _paymentCards.asStateFlow()

    private val _upiAccounts = MutableStateFlow(repository.getDefaultUpiAccounts())
    val upiAccounts: StateFlow<List<UpiAccount>> = _upiAccounts.asStateFlow()

    private val _activeOrder = MutableStateFlow(repository.getInitialActiveOrder())
    val activeOrder: StateFlow<ActiveOrder> = _activeOrder.asStateFlow()

    private val _pastOrders = MutableStateFlow(repository.getDefaultPastOrders())
    val pastOrders: StateFlow<List<PastOrder>> = _pastOrders.asStateFlow()

    private val _searchQuery = MutableStateFlow("Market St")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isAddressSearchOpen = MutableStateFlow(false)
    val isAddressSearchOpen: StateFlow<Boolean> = _isAddressSearchOpen.asStateFlow()

    private val _uploadedNotification = MutableStateFlow<String?>(null)
    val uploadedNotification: StateFlow<String?> = _uploadedNotification.asStateFlow()

    fun selectTab(tab: BottomTab) {
        _currentTab.value = tab
        when (tab) {
            BottomTab.HOME -> _currentScreen.value = "SET_LOCATION"
            BottomTab.ORDERS -> _currentScreen.value = "ORDER_TRACKING"
            BottomTab.PRESCRIPTIONS -> _currentScreen.value = "MY_PRESCRIPTIONS"
            BottomTab.PROFILE -> _currentScreen.value = "PROFILE"
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun openAddressSearch() {
        _isAddressSearchOpen.value = true
    }

    fun closeAddressSearch() {
        _isAddressSearchOpen.value = false
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectAddressFromSearch(title: String, subtitle: String) {
        _selectedAddress.value = DeliveryAddress(
            id = "sel_${System.currentTimeMillis()}",
            title = title,
            street = subtitle,
            cityStateZip = "San Francisco, CA",
            tag = "Home"
        )
        _isAddressSearchOpen.value = false
    }

    fun selectSavedAddress(address: DeliveryAddress) {
        _selectedAddress.value = address
        _isAddressSearchOpen.value = false
    }

    fun confirmLocation() {
        _currentScreen.value = "SELECT_PRESCRIPTION"
    }

    fun selectPrescription(prescription: PrescriptionItem) {
        _selectedPrescription.value = prescription
    }

    fun proceedToQuotesWithoutPrescription() {
        _selectedPrescription.value = PrescriptionItem(
            id = "otc_${System.currentTimeMillis()}",
            title = "Over-the-Counter / Non-Prescription Order",
            uploadDate = "Today",
            status = PrescriptionStatus.VERIFIED,
            expiryText = "OTC Items"
        )
    }

    fun selectPharmacyQuote(quote: PharmacyQuote) {
        _selectedQuote.value = quote
        _currentScreen.value = "REVIEW_PAY"
    }

    fun filterQuotesBy(mode: String) {
        val currentList = repository.getDefaultPharmacyQuotes()
        if (mode == "PRICE") {
            _quotes.value = currentList.sortedBy { it.quotedPrice }
        } else if (mode == "NEAREST") {
            _quotes.value = currentList.sortedBy { it.distanceKm }
        }
    }

    fun confirmPaymentAndOrder() {
        _currentScreen.value = "PAYMENT_SUCCESS"
    }

    fun trackOrder() {
        _currentTab.value = BottomTab.ORDERS
        _currentScreen.value = "ORDER_TRACKING"
    }

    fun uploadNewPrescription(title: String) {
        viewModelScope.launch {
            _inMemoryPrescriptions.value = null
            val newPresc = PrescriptionItem(
                id = "presc_${System.currentTimeMillis()}",
                title = title,
                uploadDate = "Today, Just Now",
                status = PrescriptionStatus.PENDING,
                expiryText = "Verification in progress"
            )
            repository.savePrescription(newPresc)
            _selectedPrescription.value = newPresc
            _uploadedNotification.value = "Prescription uploaded successfully for verification!"
            _currentTab.value = BottomTab.PRESCRIPTIONS
            _currentScreen.value = "MY_PRESCRIPTIONS"
        }
    }

    fun uploadNewPrescriptionAndContinue(title: String) {
        viewModelScope.launch {
            _inMemoryPrescriptions.value = null
            val newPresc = PrescriptionItem(
                id = "presc_${System.currentTimeMillis()}",
                title = title,
                uploadDate = "Today, Just Now",
                status = PrescriptionStatus.PENDING,
                expiryText = "Verification in progress"
            )
            repository.savePrescription(newPresc)
            _selectedPrescription.value = newPresc
            _uploadedNotification.value = "Prescription attached successfully!"
            _currentScreen.value = "QUOTES"
        }
    }

    fun deletePrescription(id: String) {
        viewModelScope.launch {
            repository.deletePrescription(id)
            val updated = prescriptions.value.filter { it.id != id }
            _inMemoryPrescriptions.value = updated
            if (_selectedPrescription.value?.id == id) {
                _selectedPrescription.value = null
            }
            _uploadedNotification.value = "Prescription deleted successfully."
        }
    }

    fun clearNotification() {
        _uploadedNotification.value = null
    }

    fun addAddress(title: String, street: String, cityZip: String, tag: String) {
        viewModelScope.launch {
            val newAddr = DeliveryAddress(
                id = "addr_${System.currentTimeMillis()}",
                title = title,
                street = street,
                cityStateZip = cityZip,
                tag = tag,
                isPrimary = false
            )
            repository.saveAddress(newAddr)
        }
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch {
            repository.deleteAddress(id)
        }
    }

    fun addPaymentCard(last4: String, expiry: String, holder: String) {
        val newCard = PaymentCard(
            id = "card_${System.currentTimeMillis()}",
            cardBrand = "VISA",
            last4 = last4,
            expiry = expiry,
            holderName = holder.uppercase(),
            isPrimary = false
        )
        _paymentCards.value = _paymentCards.value + newCard
    }

    fun addUpiAccount(upi: String) {
        val newUpi = UpiAccount(
            id = "upi_${System.currentTimeMillis()}",
            upiId = upi,
            label = "Personal UPI",
            isDefault = false
        )
        _upiAccounts.value = _upiAccounts.value + newUpi
    }

    fun deleteUpiAccount(id: String) {
        _upiAccounts.value = _upiAccounts.value.filter { it.id != id }
    }

    fun reorderPastOrder(pastOrder: PastOrder) {
        _selectedPrescription.value = PrescriptionItem(
            id = "reorder_${System.currentTimeMillis()}",
            title = "Reorder from ${pastOrder.pharmacyName}",
            uploadDate = "Today",
            status = PrescriptionStatus.VERIFIED,
            expiryText = pastOrder.itemsSummary
        )
        _currentScreen.value = "QUOTES"
    }

    fun completeAndRateActiveOrder(rating: Int, feedback: String) {
        val active = _activeOrder.value
        val completedPastOrder = PastOrder(
            orderId = active.orderId,
            orderNumber = active.orderNumber,
            pharmacyName = active.pharmacyName,
            dateText = "Just Now",
            statusText = "DELIVERED",
            itemsSummary = active.items.joinToString(", ") { "${it.name} (${it.quantityDetails})" },
            totalAmount = active.grandTotal,
            isDelivered = true
        )
        _pastOrders.value = listOf(completedPastOrder) + _pastOrders.value
        _uploadedNotification.value = "Thank you for your rating! Order ${active.orderNumber} is complete."
        selectTab(BottomTab.HOME)
    }
}

