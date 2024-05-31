package com.singing.feature.recording.setup.extra.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.ui.screen.dimens

@Composable
fun SelectVariant(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    contentColor: Color,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .heightIn(250.dp)
            .clip(shape = MaterialTheme.shapes.large)
            .background(color)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            tint = contentColor,
            contentDescription = "",
        )

        Spacer(Modifier.height(MaterialTheme.dimens.dimen1))

        Text(
            text = title,
            color = contentColor,
            fontSize = 45.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
        )

        Text(
            text = subtitle,
            color = contentColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
        )
    }
}