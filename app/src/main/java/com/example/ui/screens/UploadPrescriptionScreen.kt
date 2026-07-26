package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FileUpload
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
import com.example.data.models.BottomTab
import com.example.ui.MedicoViewModel
import com.example.ui.theme.MedicoGreenContainer
import com.example.ui.theme.MedicoPrimary

@Composable
fun UploadPrescriptionScreen(
    viewModel: MedicoViewModel,
    onUploadSuccess: () -> Unit
) {
    val context = LocalContext.current
    var prescriptionTitle by remember { mutableStateOf("") }
    var selectedPhoto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9FE))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Upload Prescription",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1B1F)
        )

        Text(
            text = "Quickly scan or upload your medical prescriptions to get quotes from verified pharmacies.",
            fontSize = 15.sp,
            color = Color(0xFF414755)
        )

        // Title Input Field
        OutlinedTextField(
            value = prescriptionTitle,
            onValueChange = { prescriptionTitle = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("prescription_title_input"),
            label = { Text("Prescription Title / Condition") },
            placeholder = { Text("e.g. Dermatology Prescription") },
            shape = RoundedCornerShape(12.dp)
        )

        // Scan or Upload Options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Camera Scan Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, Color(0xFF53E16F), RoundedCornerShape(16.dp))
                    .clickable {
                        selectedPhoto = true
                        if (prescriptionTitle.isEmpty()) prescriptionTitle = "Scanned Prescription Doc"
                        Toast.makeText(context, "Camera scan captured!", Toast.LENGTH_SHORT).show()
                    }
                    .testTag("scan_photo_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(MedicoGreenContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scan",
                            tint = Color(0xFF00531C),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Scan Photo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1B1F)
                    )
                    Text(
                        text = "Use camera",
                        fontSize = 13.sp,
                        color = Color(0xFF414755)
                    )
                }
            }

            // Gallery Upload Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, MedicoPrimary, RoundedCornerShape(16.dp))
                    .clickable {
                        selectedPhoto = true
                        if (prescriptionTitle.isEmpty()) prescriptionTitle = "Gallery Prescription PDF"
                        Toast.makeText(context, "File selected from gallery!", Toast.LENGTH_SHORT).show()
                    }
                    .testTag("gallery_upload_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color(0xFFD8E2FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = "Gallery",
                            tint = MedicoPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "From Gallery",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1B1F)
                    )
                    Text(
                        text = "PDF / Image",
                        fontSize = 13.sp,
                        color = Color(0xFF414755)
                    )
                }
            }
        }

        // Preview of Captured Document
        if (selectedPhoto) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth().testTag("prescription_photo_preview")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_prescription_sample_1785001665470),
                        contentDescription = "Selected Doc",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (prescriptionTitle.isNotEmpty()) prescriptionTitle else "Prescription Attached",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1B1F)
                        )
                        Text(
                            text = "Ready to submit for pharmacist review",
                            fontSize = 13.sp,
                            color = Color(0xFF00732A)
                        )
                    }
                }
            }
        }

        // Upload Guidelines Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE8F5E9),
            modifier = Modifier.fillMaxWidth().testTag("upload_guidelines_banner")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Guidelines for clear upload:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00531C)
                )

                Spacer(modifier = Modifier.height(8.dp))

                GuidelineItem("Doctor name and clinic header are clearly visible")
                GuidelineItem("Patient details and medication names are legible")
                GuidelineItem("Valid doctor stamp and signature present")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val titleToSave = if (prescriptionTitle.isNotEmpty()) prescriptionTitle else "General Medical Prescription"
                if (viewModel.currentTab.value == BottomTab.HOME) {
                    viewModel.uploadNewPrescriptionAndContinue(titleToSave)
                } else {
                    viewModel.uploadNewPrescription(titleToSave)
                }
                onUploadSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_prescription_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary)
        ) {
            Text(
                text = "Submit Prescription",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun GuidelineItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF00732A),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color(0xFF00531C)
        )
    }
}
