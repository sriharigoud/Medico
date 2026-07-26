package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PharmacyQuote
import com.example.ui.MedicoViewModel
import com.example.ui.theme.MedicoGreenContainer
import com.example.ui.theme.MedicoPrimary

@Composable
fun QuotesScreen(
    viewModel: MedicoViewModel,
    onQuoteSelected: (PharmacyQuote) -> Unit
) {
    val quotes by viewModel.quotes.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val selectedPrescription by viewModel.selectedPrescription.collectAsState()
    var selectedFilter by remember { mutableStateOf("PRICE") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9FE))
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Finding the best prices for you",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1B1F)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${quotes.size} verified pharmacy quotes calculated for your location & prescription",
                    fontSize = 13.sp,
                    color = Color(0xFF414755)
                )
            }

            // Order Context Header Card (Location & Prescription)
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth().testTag("quotes_context_header_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Location Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "DELIVERY LOCATION",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF414755),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${selectedAddress.title} (${selectedAddress.street})",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1B1F),
                                    maxLines = 1
                                )
                            }
                            Text(
                                text = "Change",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MedicoPrimary,
                                modifier = Modifier
                                    .clickable { viewModel.navigateTo("SET_LOCATION") }
                                    .padding(4.dp)
                                    .testTag("quotes_change_location_button")
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFEEEDF3))

                        // Prescription Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "ATTACHED PRESCRIPTION / ORDER TYPE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF414755),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = selectedPrescription?.title ?: "Dermatology / General Medical Rx",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00531C),
                                    maxLines = 1
                                )
                            }
                            Text(
                                text = "Change",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MedicoPrimary,
                                modifier = Modifier
                                    .clickable { viewModel.navigateTo("SELECT_PRESCRIPTION") }
                                    .padding(4.dp)
                                    .testTag("quotes_change_prescription_button")
                            )
                        }
                    }
                }
            }

            item {
                // Toggle Filters
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = selectedFilter == "PRICE",
                        onClick = {
                            selectedFilter = "PRICE"
                            viewModel.filterQuotesBy("PRICE")
                        },
                        label = { Text("Price (Low to High)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MedicoPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF4F3F8),
                            labelColor = Color(0xFF1A1B1F)
                        )
                    )

                    FilterChip(
                        selected = selectedFilter == "NEAREST",
                        onClick = {
                            selectedFilter = "NEAREST"
                            viewModel.filterQuotesBy("NEAREST")
                        },
                        label = { Text("Nearest") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MedicoPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF4F3F8),
                            labelColor = Color(0xFF1A1B1F)
                        )
                    )
                }
            }

            // Pharmacy Quote Cards
            items(quotes) { quote ->
                PharmacyQuoteCard(
                    quote = quote,
                    onSelect = {
                        viewModel.selectPharmacyQuote(quote)
                        onQuoteSelected(quote)
                    }
                )
            }
        }

        // Bottom Stepper Journey Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFF4F3F8),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .testTag("order_journey_stepper")
            ) {
                Text(
                    text = "Your Order Journey",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1B1F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Step 1: Uploaded (Completed)
                    JourneyStepIcon(title = "Uploaded", isDone = true, isActive = false)
                    JourneyLine(isDone = true)

                    // Step 2: Reviewing (Completed)
                    JourneyStepIcon(title = "Reviewing", isDone = true, isActive = false)
                    JourneyLine(isDone = true)

                    // Step 3: Quotes (Active)
                    JourneyStepIcon(title = "Quotes", isDone = false, isActive = true)
                    JourneyLine(isDone = false)

                    // Step 4: Delivery (Pending)
                    JourneyStepIcon(title = "Delivery", isDone = false, isActive = false)
                }
            }
        }
    }
}

@Composable
private fun PharmacyQuoteCard(
    quote: PharmacyQuote,
    onSelect: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE9E7ED), RoundedCornerShape(16.dp))
            .testTag("pharmacy_quote_card_${quote.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFE8F0FE), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalPharmacy,
                            contentDescription = null,
                            tint = MedicoPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = quote.pharmacyName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1B1F)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFB800),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${quote.rating} • ${quote.distanceKm} km",
                                fontSize = 13.sp,
                                color = Color(0xFF414755)
                            )
                        }
                    }
                }

                // Badge
                val badgeBg = when (quote.badgeType) {
                    "In Stock", "Nearest" -> MedicoGreenContainer
                    else -> Color(0xFFD8E2FF)
                }
                val badgeText = when (quote.badgeType) {
                    "In Stock", "Nearest" -> Color(0xFF00531C)
                    else -> MedicoPrimary
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = quote.badgeType,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Quoted Price",
                        fontSize = 12.sp,
                        color = Color(0xFF414755)
                    )
                    Text(
                        text = "\$${String.format("%.2f", quote.quotedPrice)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MedicoPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Secure",
                            tint = Color(0xFF414755),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Secure Payment Required",
                            fontSize = 11.sp,
                            color = Color(0xFF414755)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Estimated Arrival",
                        fontSize = 12.sp,
                        color = Color(0xFF414755)
                    )
                    Text(
                        text = quote.estimatedArrival,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (quote.isLowStock) Color(0xFFBA1A1A) else Color(0xFF1A1B1F)
                    )
                    if (quote.isLowStock) {
                        Text(
                            text = "Low Stock",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBA1A1A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Review & Pay",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun JourneyStepIcon(title: String, isDone: Boolean, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = when {
                        isDone -> Color(0xFF00732A)
                        isActive -> MedicoPrimary
                        else -> Color(0xFFE3E2E7)
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isDone -> Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                isActive -> Icon(Icons.Default.LocalPharmacy, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                else -> Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF717786), modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isActive || isDone) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) MedicoPrimary else Color(0xFF414755)
        )
    }
}

@Composable
private fun RowScope.JourneyLine(isDone: Boolean) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(3.dp)
            .background(if (isDone) Color(0xFF00732A) else Color(0xFFE3E2E7))
            .padding(horizontal = 4.dp)
    )
}
