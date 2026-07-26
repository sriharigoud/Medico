package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PaymentCard
import com.example.ui.MedicoViewModel
import com.example.ui.theme.MedicoGreenContainer
import com.example.ui.theme.MedicoPrimary

@Composable
fun PaymentMethodsScreen(
    viewModel: MedicoViewModel
) {
    val paymentCards by viewModel.paymentCards.collectAsState()
    val upiAccounts by viewModel.upiAccounts.collectAsState()
    val context = LocalContext.current

    var showAddCardDialog by remember { mutableStateOf(false) }
    var cardLast4 by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }

    var showAddUpiDialog by remember { mutableStateOf(false) }
    var upiInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9FE))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Payment Methods",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1B1F)
        )

        Text(
            text = "Manage your secure payment options for quick healthcare checkouts.",
            fontSize = 14.sp,
            color = Color(0xFF414755)
        )

        // Saved Credit / Debit Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SAVED CARDS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF414755),
                letterSpacing = 0.5.sp
            )

            TextButton(
                onClick = { showAddCardDialog = true },
                modifier = Modifier.testTag("add_card_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = MedicoPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Card", color = MedicoPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(paymentCards) { card ->
                CreditCardItem(card = card)
            }
        }

        Divider(color = Color(0xFFE9E7ED))

        // UPI Accounts Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "UPI ACCOUNTS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF414755),
                letterSpacing = 0.5.sp
            )

            TextButton(
                onClick = { showAddUpiDialog = true },
                modifier = Modifier.testTag("add_upi_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = MedicoPrimary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add UPI", color = MedicoPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        upiAccounts.forEach { upi ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upi_item_${upi.id}")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFE8F0FE), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                tint = MedicoPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = upi.upiId,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1B1F)
                            )
                            Text(
                                text = upi.label,
                                fontSize = 12.sp,
                                color = Color(0xFF414755)
                            )
                        }
                    }

                    if (upi.isDefault) {
                        Surface(shape = RoundedCornerShape(12.dp), color = MedicoGreenContainer) {
                            Text("DEFAULT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00531C), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    } else {
                        IconButton(onClick = { viewModel.deleteUpiAccount(upi.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFBA1A1A), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }

        Divider(color = Color(0xFFE9E7ED))

        // Digital Wallet Section
        Text(
            text = "DIGITAL WALLET",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF414755),
            letterSpacing = 0.5.sp
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth().testTag("medpay_wallet_card")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFD4F8D8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color(0xFF00732A),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "MedPay Wallet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1B1F)
                        )
                        Text(
                            text = "Available Balance: \$240.50",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00732A)
                        )
                    }
                }

                Button(
                    onClick = { Toast.makeText(context, "Wallet refill requested", Toast.LENGTH_SHORT).show() },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicoPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Refill", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // PCI-DSS Security Callout
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE8F0FE),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MedicoPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "PCI-DSS Level 1 Encrypted",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MedicoPrimary
                    )
                    Text(
                        text = "Your card details are tokenized & compliant with global security standards.",
                        fontSize = 12.sp,
                        color = Color(0xFF414755)
                    )
                }
            }
        }
    }

    if (showAddCardDialog) {
        AlertDialog(
            onDismissRequest = { showAddCardDialog = false },
            title = { Text("Add Payment Card") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = cardLast4, onValueChange = { cardLast4 = it }, label = { Text("Last 4 Digits") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = cardExpiry, onValueChange = { cardExpiry = it }, label = { Text("Expiry (MM/YY)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = cardHolder, onValueChange = { cardHolder = it }, label = { Text("Cardholder Name") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (cardLast4.isNotEmpty()) {
                            viewModel.addPaymentCard(cardLast4, if (cardExpiry.isNotEmpty()) cardExpiry else "09/28", if (cardHolder.isNotEmpty()) cardHolder else "CHRISTOPHER HENDERSON")
                            showAddCardDialog = false
                            Toast.makeText(context, "Card added!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showAddCardDialog = false }) { Text("Cancel") } }
        )
    }

    if (showAddUpiDialog) {
        AlertDialog(
            onDismissRequest = { showAddUpiDialog = false },
            title = { Text("Add UPI ID") },
            text = {
                OutlinedTextField(value = upiInput, onValueChange = { upiInput = it }, label = { Text("UPI ID (e.g. user@okaxis)") }, modifier = Modifier.fillMaxWidth())
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (upiInput.contains("@")) {
                            viewModel.addUpiAccount(upiInput)
                            showAddUpiDialog = false
                            Toast.makeText(context, "UPI added!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showAddUpiDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun CreditCardItem(card: PaymentCard) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .width(260.dp)
            .height(150.dp)
            .testTag("card_item_${card.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF0058BC), Color(0xFF003882))
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card.cardBrand,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (card.isPrimary) {
                        Surface(shape = RoundedCornerShape(10.dp), color = Color.White.copy(alpha = 0.2f)) {
                            Text("PRIMARY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }

                Text(
                    text = "•••• •••• •••• ${card.last4}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("CARD HOLDER", fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                        Text(card.holderName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Column {
                        Text("EXPIRES", fontSize = 9.sp, color = Color.White.copy(alpha = 0.7f))
                        Text(card.expiry, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
