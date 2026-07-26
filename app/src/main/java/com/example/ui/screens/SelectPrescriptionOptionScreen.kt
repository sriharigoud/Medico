package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.ShoppingBag
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
import com.example.data.models.PrescriptionItem
import com.example.data.models.PrescriptionStatus
import com.example.ui.MedicoViewModel
import com.example.ui.theme.MedicoGreenContainer
import com.example.ui.theme.MedicoPrimary

@Composable
fun SelectPrescriptionOptionScreen(
    viewModel: MedicoViewModel,
    onChangeAddressClick: () -> Unit,
    onUploadNewClick: () -> Unit,
    onProceedToQuotes: () -> Unit
) {
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val prescriptions by viewModel.prescriptions.collectAsState()
    val selectedPrescription by viewModel.selectedPrescription.collectAsState()

    var activeSelectedId by remember(selectedPrescription) {
        mutableStateOf(selectedPrescription?.id ?: prescriptions.firstOrNull()?.id ?: "")
    }

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
            // Step Progress Indicator
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth().testTag("prescription_progress_bar")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StepDot(label = "Location", isDone = true, isActive = false)
                        Box(modifier = Modifier.width(30.dp).height(2.dp).background(Color(0xFF00732A)))
                        StepDot(label = "Prescription", isDone = false, isActive = true)
                        Box(modifier = Modifier.width(30.dp).height(2.dp).background(Color(0xFFE3E2E7)))
                        StepDot(label = "Quotes", isDone = false, isActive = false)
                    }
                }
            }

            // Delivery Location Header Banner
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth().testTag("confirmed_location_card")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFD8E2FF), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MedicoPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "DELIVERING TO",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF414755),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = selectedAddress.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1B1F)
                                )
                                Text(
                                    text = selectedAddress.street,
                                    fontSize = 13.sp,
                                    color = Color(0xFF717786),
                                    maxLines = 1
                                )
                            }
                        }

                        TextButton(
                            onClick = onChangeAddressClick,
                            modifier = Modifier.testTag("change_address_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditLocation,
                                contentDescription = "Change Address",
                                tint = MedicoPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Change",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MedicoPrimary
                            )
                        }
                    }
                }
            }

            // Screen Header Title
            item {
                Text(
                    text = "Select Prescription Option",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1B1F)
                )
                Text(
                    text = "Choose an existing prescription or upload a new scan to receive exact pharmacy quotes.",
                    fontSize = 14.sp,
                    color = Color(0xFF414755)
                )
            }

            // Option A: Upload New Prescription Banner Button
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE8F5E9),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF00732A)),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onUploadNewClick() }
                        .testTag("upload_new_prescription_card")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFF00732A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FileUpload,
                                contentDescription = "Upload",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Upload New Prescription",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00531C)
                            )
                            Text(
                                text = "Scan Rx document with camera or pick PDF/Image from gallery",
                                fontSize = 12.sp,
                                color = Color(0xFF00531C)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFF00531C)
                        )
                    }
                }
            }

            // Option B: Select Saved Prescription Header
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "OR SELECT FROM MY PRESCRIPTIONS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF414755),
                        letterSpacing = 0.5.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFD8E2FF)
                    ) {
                        Text(
                            text = "${prescriptions.size} Available",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicoPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // List of Saved Prescriptions
            items(prescriptions) { item ->
                val isSelected = activeSelectedId == item.id
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFFEEF3FF) else Color.White,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MedicoPrimary else Color(0xFFE9E7ED)
                    ),
                    shadowElevation = if (isSelected) 4.dp else 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activeSelectedId = item.id
                            viewModel.selectPrescription(item)
                        }
                        .testTag("saved_prescription_item_${item.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    if (isSelected) MedicoPrimary else Color(0xFFF4F3F8),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color(0xFF717786),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1B1F)
                            )
                            Text(
                                text = "Uploaded: ${item.uploadDate} • ${item.expiryText}",
                                fontSize = 12.sp,
                                color = Color(0xFF717786)
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            val statusColor = when (item.status) {
                                PrescriptionStatus.VERIFIED -> Color(0xFF00732A)
                                PrescriptionStatus.PENDING -> Color(0xFFE65100)
                                PrescriptionStatus.EXPIRED -> Color(0xFFBA1A1A)
                            }
                            val statusBg = when (item.status) {
                                PrescriptionStatus.VERIFIED -> MedicoGreenContainer
                                PrescriptionStatus.PENDING -> Color(0xFFFFF3E0)
                                PrescriptionStatus.EXPIRED -> Color(0xFFFFDAD6)
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = statusBg
                            ) {
                                Text(
                                    text = item.status.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.deletePrescription(item.id) },
                                modifier = Modifier.size(32.dp).testTag("delete_saved_rx_${item.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFBA1A1A),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Select",
                                tint = if (isSelected) MedicoPrimary else Color(0xFFC4C6CF),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Option C: Order Direct OTC
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9E7ED)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.proceedToQuotesWithoutPrescription()
                            onProceedToQuotes()
                        }
                        .testTag("otc_direct_order_card")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFF4F3F8), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = Color(0xFF414755),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Order Non-Prescription / OTC Items",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1B1F)
                            )
                            Text(
                                text = "Browse quotes for wellness products, vitamins & general healthcare",
                                fontSize = 12.sp,
                                color = Color(0xFF717786)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFF717786)
                        )
                    }
                }
            }
        }

        // Bottom Proceed Button Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val chosen = prescriptions.find { it.id == activeSelectedId }
                        if (chosen != null) {
                            viewModel.selectPrescription(chosen)
                        } else {
                            viewModel.proceedToQuotesWithoutPrescription()
                        }
                        onProceedToQuotes()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("proceed_to_quotes_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Get Pharmacy Quotes",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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
}

@Composable
private fun StepDot(label: String, isDone: Boolean, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
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
            if (isDone) {
                Text(text = "✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            } else {
                Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isActive || isDone) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive || isDone) MedicoPrimary else Color(0xFF717786)
        )
    }
}
