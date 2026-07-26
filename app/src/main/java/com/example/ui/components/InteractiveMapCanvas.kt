package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MedicoPrimary

@Composable
fun InteractiveMapCanvas(
    modifier: Modifier = Modifier,
    centerPinText: String = "Deliver here",
    showCourierProgress: Boolean = false,
    courierProgress: Float = 0.6f,
    showZoomControls: Boolean = false,
    onZoomIn: () -> Unit = {},
    onZoomOut: () -> Unit = {}
) {
    val textMeasurer = rememberTextMeasurer()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Water Background
            drawRect(color = Color(0xFFC7DEFF))

            // 2. San Francisco Landmass Path
            val landPath = Path().apply {
                moveTo(0f, height * 0.15f)
                cubicTo(width * 0.3f, height * 0.12f, width * 0.7f, height * 0.18f, width, height * 0.1f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(path = landPath, color = Color(0xFFF3F1EA))

            // 3. Bay / Coastline stroke
            drawPath(
                path = landPath,
                color = Color(0xFFA5C8FE),
                style = Stroke(width = 3f)
            )

            // 4. Parks / Presidio / Golden Gate Park
            val parkColor = Color(0xFFD3EBC6)
            drawRoundRect(
                color = parkColor,
                topLeft = Offset(width * 0.05f, height * 0.22f),
                size = Size(width * 0.25f, height * 0.12f)
            )
            drawRoundRect(
                color = parkColor,
                topLeft = Offset(width * 0.08f, height * 0.58f),
                size = Size(width * 0.35f, height * 0.06f)
            )

            // 5. Road Network Grid Lines
            val roadColor = Color(0xFFFFFFFF)
            val mainHighwayColor = Color(0xFFFFDB8B)

            // Major grid streets
            for (i in 1..8) {
                drawLine(
                    color = roadColor,
                    start = Offset(0f, height * (0.2f + i * 0.08f)),
                    end = Offset(width, height * (0.2f + i * 0.08f)),
                    strokeWidth = 6f
                )
                drawLine(
                    color = roadColor,
                    start = Offset(width * (i * 0.12f), height * 0.15f),
                    end = Offset(width * (i * 0.12f), height),
                    strokeWidth = 6f
                )
            }

            // Diagonal Highway US 101 / Market St
            val highwayPath = Path().apply {
                moveTo(width * 0.15f, height * 0.2f)
                lineTo(width * 0.5f, height * 0.5f)
                lineTo(width * 0.75f, height * 0.85f)
            }
            drawPath(
                path = highwayPath,
                color = mainHighwayColor,
                style = Stroke(width = 12f)
            )

            // 6. Map Labels
            val labels = listOf(
                Pair("PRESIDIO OF SAN FRANCISCO", Offset(width * 0.1f, height * 0.25f)),
                Pair("CHINATOWN", Offset(width * 0.52f, height * 0.30f)),
                Pair("UNION SQUARE", Offset(width * 0.52f, height * 0.35f)),
                Pair("FINANCIAL DISTRICT", Offset(width * 0.60f, height * 0.40f)),
                Pair("MISSION DISTRICT", Offset(width * 0.55f, height * 0.52f)),
                Pair("NOE VALLEY", Offset(width * 0.45f, height * 0.60f)),
                Pair("SUNSET DISTRICT", Offset(width * 0.12f, height * 0.50f))
            )

            for ((text, pos) in labels) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = text,
                    topLeft = pos,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7A8293)
                    )
                )
            }

            // 7. Route & Courier tracking layer if showCourierProgress
            if (showCourierProgress) {
                val routePath = Path().apply {
                    moveTo(width * 0.7f, height * 0.35f)
                    lineTo(width * 0.55f, height * 0.45f)
                    lineTo(width * 0.5f, height * 0.5f)
                }
                drawPath(
                    path = routePath,
                    color = MedicoPrimary,
                    style = Stroke(width = 8f)
                )

                // Current courier animated position
                val courierX = width * (0.7f - courierProgress * 0.2f)
                val courierY = height * (0.35f + courierProgress * 0.15f)

                drawCircle(
                    color = Color(0x550058BC),
                    radius = 24f * pulseScale,
                    center = Offset(courierX, courierY)
                )
                drawCircle(
                    color = MedicoPrimary,
                    radius = 16f,
                    center = Offset(courierX, courierY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 8f,
                    center = Offset(courierX, courierY)
                )
            }
        }

        // Center Marker Overlay
        Box(
            modifier = Modifier.align(Alignment.Center),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = centerPinText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1B1F),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MedicoPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Marker",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Optional Zoom Controls (+ / -)
        if (showZoomControls) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .testTag("zoom_in_button")
                ) {
                    IconButton(onClick = onZoomIn) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Zoom In", tint = Color.Black)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .testTag("zoom_out_button")
                ) {
                    IconButton(onClick = onZoomOut) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Zoom Out", tint = Color.Black)
                    }
                }
            }
        }
    }
}
