package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.data.models.PrescriptionItem
import com.example.data.models.PrescriptionStatus
import com.example.ui.MedicoViewModel
import com.example.ui.theme.MedicoGreenContainer
import com.example.ui.theme.MedicoPrimary

@Composable
fun PrescriptionsListScreen(
    viewModel: MedicoViewModel,
    onUploadClick: () -> Unit
) {
    val prescriptions by viewModel.prescriptions.collectAsState()
    val notification by viewModel.uploadedNotification.collectAsState()

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
                    text = "My Prescriptions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1B1F)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Manage and track your active and historical medical prescriptions.",
                    fontSize = 15.sp,
                    color = Color(0xFF414755)
                )

                if (notification != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MedicoGreenContainer,
                        modifier = Modifier.fillMaxWidth().testTag("prescription_notification_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = notification ?: "",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00531C),
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { viewModel.clearNotification() }) {
                                Text("Dismiss", color = Color(0xFF00531C), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            items(prescriptions) { item ->
                PrescriptionCard(
                    prescription = item,
                    onUploadClick = onUploadClick,
                    onDeleteClick = { viewModel.deletePrescription(item.id) }
                )
            }
        }

        // Bottom Upload New Prescription Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Button(
                onClick = onUploadClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp)
                    .testTag("upload_new_prescription_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Upload New Prescription",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun PrescriptionCard(
    prescription: PrescriptionItem,
    onUploadClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Prescription", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete '${prescription.title}'? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A))
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel", color = Color(0xFF414755))
                }
            }
        )
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE9E7ED), RoundedCornerShape(16.dp))
            .testTag("prescription_card_${prescription.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_prescription_sample_1785001665470),
                        contentDescription = prescription.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = prescription.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1B1F)
                        )
                        Text(
                            text = "Uploaded ${prescription.uploadDate}",
                            fontSize = 13.sp,
                            color = Color(0xFF414755)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Badge
                    val (badgeBg, badgeText, statusText) = when (prescription.status) {
                        PrescriptionStatus.VERIFIED -> Triple(MedicoGreenContainer, Color(0xFF00531C), "VERIFIED")
                        PrescriptionStatus.PENDING -> Triple(Color(0xFFFFF3CD), Color(0xFF856404), "PENDING")
                        PrescriptionStatus.EXPIRED -> Triple(Color(0xFFFFDAD6), Color(0xFFBA1A1A), "EXPIRED")
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = badgeBg
                    ) {
                        Text(
                            text = statusText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.testTag("delete_prescription_${prescription.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Prescription",
                            tint = Color(0xFFBA1A1A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = prescription.expiryText,
                    fontSize = 13.sp,
                    color = Color(0xFF414755)
                )

                if (prescription.status == PrescriptionStatus.EXPIRED) {
                    TextButton(onClick = onUploadClick) {
                        Text("Renew", color = MedicoPrimary, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { }
                    ) {
                        Text(
                            text = "View",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicoPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MedicoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
