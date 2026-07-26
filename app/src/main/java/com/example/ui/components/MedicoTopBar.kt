package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.MedicoPrimary

@Composable
fun MedicoTopBar(
    title: String = "Medico",
    showBack: Boolean = false,
    showDrawer: Boolean = false,
    showSearch: Boolean = false,
    showNotification: Boolean = false,
    onBackClick: () -> Unit = {},
    onDrawerClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(Color(0xFFFAF9FE))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (showBack) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("topbar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MedicoPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            } else if (showDrawer) {
                IconButton(
                    onClick = onDrawerClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("topbar_drawer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MedicoPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MedicoPrimary,
                modifier = Modifier.testTag("topbar_title")
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showSearch) {
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("topbar_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MedicoPrimary
                    )
                }
            }

            if (showNotification) {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("topbar_notification_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MedicoPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // User Doctor Avatar
            Image(
                painter = painterResource(id = R.drawable.img_user_avatar_1785001615929),
                contentDescription = "User Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFD8E2FF), CircleShape)
                    .clickable { onAvatarClick() }
                    .testTag("topbar_user_avatar")
            )
        }
    }
}
