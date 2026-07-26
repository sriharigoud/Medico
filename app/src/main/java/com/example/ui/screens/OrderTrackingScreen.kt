package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.DeliveryStepStatus
import com.example.ui.MedicoViewModel
import com.example.ui.components.InteractiveMapCanvas
import com.example.ui.theme.MedicoGreenContainer
import com.example.ui.theme.MedicoPrimary

@Composable
fun OrderTrackingScreen(
    viewModel: MedicoViewModel,
    onRateDeliveryClick: () -> Unit
) {
    val activeOrder by viewModel.activeOrder.collectAsState()
    val pastOrders by viewModel.pastOrders.collectAsState()
    val context = LocalContext.current

    var courierProgress by remember { mutableStateOf(0.6f) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9FE))
            .verticalScroll(rememberScrollState())
    ) {
        // Map Tracking Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            InteractiveMapCanvas(
                centerPinText = "Your Location",
                showCourierProgress = true,
                courierProgress = courierProgress
            )

            // ETA Pill Badge over Map
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(20.dp),
                color = MedicoGreenContainer,
                shadowElevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF00732A), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Arriving in ${activeOrder.etaMinutes} mins",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00531C)
                    )
                }
            }
        }

        // Tracking Content Details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Courier Partner Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth().testTag("courier_info_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MedicoGreenContainer
                        ) {
                            Text(
                                text = "ON THE WAY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00531C),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(onClick = {
                            Toast.makeText(context, "Support assistant connected!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = "Help",
                                tint = MedicoPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_courier_partner_1785001631588),
                            contentDescription = activeOrder.courierName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .border(2.dp, MedicoPrimary, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeOrder.courierName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1B1F)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB800),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${activeOrder.courierRating} • ${activeOrder.courierRole}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF414755)
                                )
                            }
                        }

                        // Call & Chat Buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFD8E2FF),
                                modifier = Modifier
                                    .size(44.dp)
                                    .clickable {
                                        Toast.makeText(context, "Calling ${activeOrder.courierName}...", Toast.LENGTH_SHORT).show()
                                    }
                                    .testTag("call_courier_button"),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call",
                                        tint = MedicoPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFD8E2FF),
                                modifier = Modifier
                                    .size(44.dp)
                                    .clickable {
                                        Toast.makeText(context, "Opening chat with ${activeOrder.courierName}...", Toast.LENGTH_SHORT).show()
                                    }
                                    .testTag("chat_courier_button"),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Message,
                                        contentDescription = "Message",
                                        tint = MedicoPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Live Order Steps Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth().testTag("live_tracking_steps_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "DELIVERY PROGRESS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF414755),
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    activeOrder.trackingSteps.forEachIndexed { index, step ->
                        Row(verticalAlignment = Alignment.Top) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            color = when (step.status) {
                                                DeliveryStepStatus.COMPLETED -> Color(0xFF00732A)
                                                DeliveryStepStatus.ACTIVE -> MedicoPrimary
                                                DeliveryStepStatus.PENDING -> Color(0xFFE3E2E7)
                                            },
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (step.status == DeliveryStepStatus.COMPLETED) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    } else if (step.status == DeliveryStepStatus.ACTIVE) {
                                        Box(modifier = Modifier.size(10.dp).background(Color.White, CircleShape))
                                    }
                                }

                                if (index < activeOrder.trackingSteps.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(36.dp)
                                            .background(
                                                if (step.status == DeliveryStepStatus.COMPLETED) Color(0xFF00732A) else Color(0xFFE3E2E7)
                                            )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = step.title,
                                    fontSize = 15.sp,
                                    fontWeight = if (step.status != DeliveryStepStatus.PENDING) FontWeight.Bold else FontWeight.Medium,
                                    color = if (step.status != DeliveryStepStatus.PENDING) Color(0xFF1A1B1F) else Color(0xFF717786)
                                )
                                Text(
                                    text = step.description,
                                    fontSize = 13.sp,
                                    color = Color(0xFF414755)
                                )
                            }
                        }
                    }
                }
            }

            // Simulate Delivered Button
            Button(
                onClick = onRateDeliveryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("mark_delivered_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Complete Order & Rate",
                        fontSize = 16.sp,
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

            Spacer(modifier = Modifier.height(8.dp))

            // Section Header: Order History & Previous Orders
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ORDER HISTORY & PREVIOUS ORDERS",
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
                        text = "${pastOrders.size} Completed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MedicoPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            pastOrders.forEach { past ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("past_order_card_${past.orderId}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = past.pharmacyName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A1B1F)
                                )
                                Text(
                                    text = "${past.orderNumber} • ${past.dateText}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF717786)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MedicoGreenContainer
                            ) {
                                Text(
                                    text = past.statusText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00531C),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = past.itemsSummary,
                            fontSize = 13.sp,
                            color = Color(0xFF414755)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "\$${String.format("%.2f", past.totalAmount)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MedicoPrimary
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        Toast.makeText(context, "Downloading invoice for ${past.orderNumber}...", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("Invoice", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        viewModel.reorderPastOrder(past)
                                        Toast.makeText(context, "Reordering items from ${past.pharmacyName}...", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("reorder_button_${past.orderId}")
                                ) {
                                    Text("Reorder", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

