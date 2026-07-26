package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MedicoViewModel
import com.example.ui.theme.MedicoGreenContainer
import com.example.ui.theme.MedicoPrimary

@Composable
fun ReviewPayScreen(
    viewModel: MedicoViewModel,
    onConfirmClick: () -> Unit,
    onChangeAddressClick: () -> Unit,
    onChangePaymentClick: () -> Unit
) {
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val orderItems by viewModel.orderItems.collectAsState()
    val selectedQuote by viewModel.selectedQuote.collectAsState()

    val subtotal = orderItems.sumOf { it.price }
    val tax = subtotal * 0.08
    val grandTotal = subtotal + tax

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9FE))
    ) {
        // Scrollable content section
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Progress Dots
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StepProgressDot(label = "Prescription", isDone = true, isActive = false)
                    Box(modifier = Modifier.width(30.dp).height(2.dp).background(Color(0xFF00732A)))
                    StepProgressDot(label = "Payment", isDone = false, isActive = true)
                    Box(modifier = Modifier.width(30.dp).height(2.dp).background(Color(0xFFE3E2E7)))
                    StepProgressDot(label = "Confirm", isDone = false, isActive = false)
                }
            }

            // Order Items Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().testTag("review_order_items_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ORDER ITEMS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF414755),
                            letterSpacing = 0.5.sp
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFD8E2FF)
                        ) {
                            Text(
                                text = "${orderItems.size} Items",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MedicoPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    orderItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_medicine_sample_1785001645525),
                                contentDescription = item.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1B1F)
                                )
                                Text(
                                    text = "${item.quantityDetails} • ${item.packType}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF414755)
                                )
                            }

                            Text(
                                text = "\$${String.format("%.2f", item.price)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MedicoPrimary
                            )
                        }
                        Divider(color = Color(0xFFEEEDF3))
                    }
                }
            }

            // Delivery Address Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().testTag("review_delivery_address_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DELIVERY ADDRESS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF414755),
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = "Change",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicoPrimary,
                            modifier = Modifier
                                .clickable { onChangeAddressClick() }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFD8E2FF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MedicoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = selectedAddress.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1B1F)
                            )
                            Text(
                                text = "${selectedAddress.street}, ${selectedAddress.cityStateZip}",
                                fontSize = 13.sp,
                                color = Color(0xFF414755)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ETA: Today, 4:00 PM - 6:00 PM",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00732A)
                            )
                        }
                    }
                }
            }

            // Payment Method Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().testTag("review_payment_method_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PAYMENT METHOD",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF414755),
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = "Manage",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicoPrimary,
                            modifier = Modifier
                                .clickable { onChangePaymentClick() }
                                .padding(4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFE8F0FE), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = null,
                                tint = MedicoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Visa •••• 4242",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1B1F)
                            )
                            Text(
                                text = "Expires 09/26",
                                fontSize = 13.sp,
                                color = Color(0xFF414755)
                            )
                        }
                    }
                }
            }

            // Order Summary Breakdown
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().testTag("review_order_summary_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ORDER SUMMARY",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF414755),
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SummaryRow(label = "Subtotal", value = "\$${String.format("%.2f", subtotal)}")
                    SummaryRow(label = "Tax (8%)", value = "\$${String.format("%.2f", tax)}")
                    SummaryRow(label = "Delivery Fee", value = "FREE", valueColor = Color(0xFF00732A))

                    Divider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFEEEDF3))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1B1F)
                        )
                        Text(
                            text = "\$${String.format("%.2f", grandTotal)}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicoPrimary
                        )
                    }
                }
            }

            // SSL Encrypted Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF00732A),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "256-bit SSL Encrypted Secure Checkout",
                    fontSize = 12.sp,
                    color = Color(0xFF00732A),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Sticky Bottom Checkout Action Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Amount",
                            fontSize = 12.sp,
                            color = Color(0xFF414755)
                        )
                        Text(
                            text = "\$${String.format("%.2f", grandTotal)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicoPrimary
                        )
                    }

                    Button(
                        onClick = onConfirmClick,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                            .height(50.dp)
                            .testTag("pay_confirm_order_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Pay & Confirm",
                                fontSize = 16.sp,
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

                Text(
                    text = "By clicking confirm, you agree to our terms & clinical guidelines.",
                    fontSize = 10.sp,
                    color = Color(0xFF717786),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, valueColor: Color = Color(0xFF1A1B1F)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF414755))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
private fun StepProgressDot(label: String, isDone: Boolean, isActive: Boolean) {
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
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Text(
                    text = if (isActive) "2" else "3",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isActive || isDone) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) MedicoPrimary else Color(0xFF717786)
        )
    }
}
