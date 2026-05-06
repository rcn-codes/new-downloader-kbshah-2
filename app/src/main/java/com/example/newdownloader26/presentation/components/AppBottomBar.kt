package com.example.newdownloader26.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newdownloader26.R
import com.example.newdownloader26.core.ui.singleClick

enum class BottomDestination {
    HOME,
    DOWNLOADS
}

@Composable
fun AppBottomBar(
    selected: BottomDestination,
    onHomeClick: () -> Unit,
    onDownloadsClick: () -> Unit
) {
    val isHomeSelected = selected == BottomDestination.HOME
    val isDownloadsSelected = selected == BottomDestination.DOWNLOADS
    val activeColor = Color(0xFFF22B29)
    val inactiveColor = Color(0xFF8A9099)

    Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        HorizontalDivider(color = Color(0xFFF1F1F1), thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 15.dp, horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clickable(onClick = singleClick(onClick = onHomeClick))
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Transparent)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_home),
                    contentDescription = stringResource(R.string.nav_home),
                    tint = if (isHomeSelected) activeColor else inactiveColor,
                    modifier = Modifier
                        .size(24.dp)
                )
                Text(
                    stringResource(R.string.nav_home),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isHomeSelected) activeColor else inactiveColor
                )
            }
            Box(modifier = Modifier.background(Color(0xFFE8E8E8)).size(width = 0.5.dp, height = 24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clickable(onClick = singleClick(onClick = onDownloadsClick))
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Transparent)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_downloaded),
                    contentDescription = stringResource(R.string.nav_downloads),
                    tint = if (isDownloadsSelected) activeColor else inactiveColor,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    stringResource(R.string.nav_downloads),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isDownloadsSelected) activeColor else inactiveColor
                )
            }
        }
    }
}