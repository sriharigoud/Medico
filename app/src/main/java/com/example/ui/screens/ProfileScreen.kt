package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
fun ProfileScreen(
    viewModel: MedicoViewModel,
    onSavedAddressesClick: () -> Unit,
    onPaymentMethodsClick: () -> Unit,
    onPrescriptionsClick: () -> Unit,
    onOrderHistoryClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9FE))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header Card
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().testTag("profile_header_card")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.img_user_avatar_1785001615929),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(2.dp, MedicoPrimary, CircleShape)
                    )

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(MedicoPrimary, CircleShape)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Christopher Henderson",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1B1F)
                    )
                    Text(
                        text = "+1 (555) 0123-4567",
                        fontSize = 13.sp,
                        color = Color(0xFF414755)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MedicoGreenContainer
                    ) {
                        Text(
                            text = "Verified Patient",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00531C),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Subscription Banner
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MedicoPrimary,
            modifier = Modifier.fillMaxWidth().testTag("medico_plus_banner")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Medico Plus Active",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Enjoy unlimited free deliveries & priority health consultations",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Button(
                    onClick = { Toast.makeText(context, "Plan details opened", Toast.LENGTH_SHORT).show() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Manage", color = MedicoPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // 2-Column Menu Grid Cards
        Text(
            text = "ACCOUNT & CLINICAL SERVICES",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF414755),
            letterSpacing = 0.5.sp
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Personal Info",
                    subtitle = "Medical profile & vitals",
                    icon = Icons.Default.Person,
                    iconBg = Color(0xFFE8F0FE),
                    onClick = { Toast.makeText(context, "Personal Info clicked", Toast.LENGTH_SHORT).show() },
                    testTag = "menu_personal_info"
                )

                ProfileMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Saved Addresses",
                    subtitle = "Manage locations",
                    icon = Icons.Default.LocationOn,
                    iconBg = Color(0xFFD4F8D8),
                    onClick = onSavedAddressesClick,
                    testTag = "menu_saved_addresses"
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Order History",
                    subtitle = "Track deliveries",
                    icon = Icons.Default.History,
                    iconBg = Color(0xFFE8F0FE),
                    onClick = onOrderHistoryClick,
                    testTag = "menu_order_history"
                )

                ProfileMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Payment Methods",
                    subtitle = "Cards & UPI wallet",
                    icon = Icons.Default.CreditCard,
                    iconBg = Color(0xFFFFF3CD),
                    onClick = onPaymentMethodsClick,
                    testTag = "menu_payment_methods"
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "My Prescriptions",
                    subtitle = "Upload & renew",
                    icon = Icons.Default.Description,
                    iconBg = Color(0xFFD8E2FF),
                    onClick = onPrescriptionsClick,
                    testTag = "menu_my_prescriptions"
                )

                ProfileMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Help & Support",
                    subtitle = "24/7 care assistant",
                    icon = Icons.Default.Help,
                    iconBg = Color(0xFFE8F0FE),
                    onClick = { Toast.makeText(context, "Help center connected", Toast.LENGTH_SHORT).show() },
                    testTag = "menu_help"
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Settings",
                    subtitle = "Privacy & security",
                    icon = Icons.Default.Settings,
                    iconBg = Color(0xFFF4F3F8),
                    onClick = { Toast.makeText(context, "Settings opened", Toast.LENGTH_SHORT).show() },
                    testTag = "menu_settings"
                )

                ProfileMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Logout",
                    subtitle = "Sign out account",
                    icon = Icons.Default.ExitToApp,
                    iconBg = Color(0xFFFFDAD6),
                    iconTint = Color(0xFFBA1A1A),
                    onClick = { Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show() },
                    testTag = "menu_logout"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Medico Clinical Version 4.2.1 • Built with precision for healthcare reliability.",
            fontSize = 12.sp,
            color = Color(0xFF717786),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun ProfileMenuCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color = MedicoPrimary,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier
            .border(1.dp, Color(0xFFE9E7ED), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1B1F)
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF414755)
            )
        }
    }
}
