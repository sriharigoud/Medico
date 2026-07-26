package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.models.BottomTab
import com.example.ui.MedicoViewModel
import com.example.ui.components.MedicoBottomBar
import com.example.ui.components.MedicoTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MedicoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicoTheme {
                MedicoApp()
            }
        }
    }
}

@Composable
fun MedicoApp(viewModel: MedicoViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    val topBarTitle = when (currentScreen) {
        "SET_LOCATION" -> "Set Delivery Location"
        "SELECT_PRESCRIPTION" -> "Prescription Options"
        "QUOTES" -> "Pharmacy Quotes"
        "REVIEW_PAY" -> "Review & Pay"
        "PAYMENT_SUCCESS" -> "Order Confirmed"
        "ORDER_TRACKING" -> "Track Order #MD-4921"
        "RATE_DELIVERY" -> "Rate Experience"
        "UPLOAD_PRESCRIPTION" -> "Upload Prescription"
        "MY_PRESCRIPTIONS" -> "My Prescriptions"
        "PROFILE" -> "Account & Profile"
        "SAVED_ADDRESSES" -> "Saved Addresses"
        "PAYMENT_METHODS" -> "Payment Methods"
        else -> "Medico"
    }

    val showBack = currentScreen in listOf(
        "SELECT_PRESCRIPTION", "QUOTES", "REVIEW_PAY", "ORDER_TRACKING", "RATE_DELIVERY",
        "UPLOAD_PRESCRIPTION", "SAVED_ADDRESSES", "PAYMENT_METHODS"
    )

    Scaffold(
        topBar = {
            MedicoTopBar(
                title = topBarTitle,
                showBack = showBack,
                showDrawer = currentScreen == "MY_PRESCRIPTIONS",
                showSearch = currentScreen in listOf("MY_PRESCRIPTIONS", "SET_LOCATION"),
                onBackClick = {
                    when (currentScreen) {
                        "SELECT_PRESCRIPTION" -> viewModel.navigateTo("SET_LOCATION")
                        "QUOTES" -> viewModel.navigateTo("SELECT_PRESCRIPTION")
                        "REVIEW_PAY" -> viewModel.navigateTo("QUOTES")
                        "ORDER_TRACKING" -> viewModel.selectTab(BottomTab.HOME)
                        "RATE_DELIVERY" -> viewModel.navigateTo("ORDER_TRACKING")
                        "UPLOAD_PRESCRIPTION" -> viewModel.navigateTo("SELECT_PRESCRIPTION")
                        "SAVED_ADDRESSES", "PAYMENT_METHODS" -> viewModel.navigateTo("PROFILE")
                        else -> viewModel.selectTab(BottomTab.HOME)
                    }
                },
                onSearchClick = {
                    if (currentScreen == "SET_LOCATION") viewModel.openAddressSearch()
                },
                onAvatarClick = {
                    viewModel.selectTab(BottomTab.PROFILE)
                }
            )
        },
        bottomBar = {
            MedicoBottomBar(
                selectedTab = currentTab,
                onTabSelected = { tab -> viewModel.selectTab(tab) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { target ->
                when (target) {
                    "SET_LOCATION" -> SetLocationScreen(
                        viewModel = viewModel,
                        onConfirmClick = { viewModel.confirmLocation() },
                        onAvatarClick = { viewModel.selectTab(BottomTab.PROFILE) }
                    )

                    "SELECT_PRESCRIPTION" -> SelectPrescriptionOptionScreen(
                        viewModel = viewModel,
                        onChangeAddressClick = { viewModel.navigateTo("SET_LOCATION") },
                        onUploadNewClick = { viewModel.navigateTo("UPLOAD_PRESCRIPTION") },
                        onProceedToQuotes = { viewModel.navigateTo("QUOTES") }
                    )

                    "QUOTES" -> QuotesScreen(
                        viewModel = viewModel,
                        onQuoteSelected = { viewModel.navigateTo("REVIEW_PAY") }
                    )

                    "REVIEW_PAY" -> ReviewPayScreen(
                        viewModel = viewModel,
                        onConfirmClick = { viewModel.confirmPaymentAndOrder() },
                        onChangeAddressClick = { viewModel.navigateTo("SAVED_ADDRESSES") },
                        onChangePaymentClick = { viewModel.navigateTo("PAYMENT_METHODS") }
                    )

                    "PAYMENT_SUCCESS" -> PaymentSuccessScreen(
                        viewModel = viewModel,
                        onTrackOrderClick = { viewModel.trackOrder() },
                        onHomeClick = { viewModel.selectTab(BottomTab.HOME) }
                    )

                    "ORDER_TRACKING" -> OrderTrackingScreen(
                        viewModel = viewModel,
                        onRateDeliveryClick = { viewModel.navigateTo("RATE_DELIVERY") }
                    )

                    "RATE_DELIVERY" -> RateDeliveryScreen(
                        viewModel = viewModel,
                        onSubmitComplete = { viewModel.selectTab(BottomTab.HOME) }
                    )

                    "UPLOAD_PRESCRIPTION" -> UploadPrescriptionScreen(
                        viewModel = viewModel,
                        onUploadSuccess = { viewModel.navigateTo("QUOTES") }
                    )

                    "MY_PRESCRIPTIONS" -> PrescriptionsListScreen(
                        viewModel = viewModel,
                        onUploadClick = { viewModel.navigateTo("UPLOAD_PRESCRIPTION") }
                    )

                    "PROFILE" -> ProfileScreen(
                        viewModel = viewModel,
                        onSavedAddressesClick = { viewModel.navigateTo("SAVED_ADDRESSES") },
                        onPaymentMethodsClick = { viewModel.navigateTo("PAYMENT_METHODS") },
                        onPrescriptionsClick = { viewModel.navigateTo("MY_PRESCRIPTIONS") },
                        onOrderHistoryClick = { viewModel.trackOrder() }
                    )

                    "SAVED_ADDRESSES" -> SavedAddressesScreen(
                        viewModel = viewModel
                    )

                    "PAYMENT_METHODS" -> PaymentMethodsScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
