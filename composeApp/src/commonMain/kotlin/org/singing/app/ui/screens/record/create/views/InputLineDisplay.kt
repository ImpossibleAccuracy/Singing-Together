package org.singing.app.ui.screens.record.create.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.singing.app.ui.base.Space


@Composable
fun InputLineDisplay(
    modifier: Modifier = Modifier,
    note: String,
    frequency: String,
    icon: ImageVector,
    contentColor: Color,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(size = 24.dp),
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
        )

        Space(8.dp)

        Text(
            text = note,
            color = contentColor,
            style = TextStyle(
                fontSize = 45.sp,
                fontWeight = FontWeight.Black
            )
        )

        Text(
            text = frequency,
            color = contentColor,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        )
    }
}
