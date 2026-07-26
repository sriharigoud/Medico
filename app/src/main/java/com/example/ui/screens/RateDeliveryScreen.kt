package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MedicoViewModel
import com.example.ui.theme.MedicoGreenContainer
import com.example.ui.theme.MedicoPrimary

@Composable
fun RateDeliveryScreen(
    viewModel: MedicoViewModel,
    onSubmitComplete: () -> Unit
) {
    val activeOrder by viewModel.activeOrder.collectAsState()
    val context = LocalContext.current

    var deliveryRating by remember { mutableIntStateOf(5) }
    var pharmacyRating by remember { mutableIntStateOf(5) }
    var feedbackText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9FE))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(72.dp)
                .background(MedicoGreenContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF00732A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            text = "Delivered successfully",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1B1F)
        )

        Text(
            text = "Your order #${activeOrder.orderId} reached you at 02:45 PM.",
            fontSize = 14.sp,
            color = Color(0xFF414755),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Rate Delivery Partner Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().testTag("rate_courier_card")
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Rate Delivery Partner (${activeOrder.courierName})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1B1F)
                )

                Spacer(modifier = Modifier.height(12.dp))

                StarRatingRow(
                    currentRating = deliveryRating,
                    onRatingChanged = { deliveryRating = it }
                )
            }
        }

        // Rate Pharmacy Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().testTag("rate_pharmacy_card")
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Rate Pharmacy (${activeOrder.pharmacyName})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1B1F)
                )

                Spacer(modifier = Modifier.height(12.dp))

                StarRatingRow(
                    currentRating = pharmacyRating,
                    onRatingChanged = { pharmacyRating = it }
                )
            }
        }

        // Feedback Text Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp)
                    .testTag("review_feedback_input"),
                placeholder = { Text("Tell us about your experience...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Thank you for your review!", Toast.LENGTH_SHORT).show()
                viewModel.completeAndRateActiveOrder(deliveryRating, feedbackText)
                onSubmitComplete()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_review_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary)
        ) {

            Text(
                text = "Submit Review",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun StarRatingRow(currentRating: Int, onRatingChanged: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (star in 1..5) {
            Icon(
                imageVector = if (star <= currentRating) Icons.Default.Star else Icons.Outlined.Star,
                contentDescription = "$star Stars",
                tint = if (star <= currentRating) Color(0xFFFFB800) else Color(0xFFC1C6D7),
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRatingChanged(star) }
            )
        }
    }
}
