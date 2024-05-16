package org.singing.app.ui.screens.account.profile.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_mic_24
import org.jetbrains.compose.resources.painterResource
import org.singing.app.ui.base.Space


@Composable
fun Banner(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .size(size = 128.dp)
                .clip(shape = RoundedCornerShape(50))
        ) {
            Image(
                painter = painterResource(Res.drawable.baseline_mic_24),
                contentDescription = "Avatar",
                modifier = Modifier.fillMaxSize()
            )
        }

        Space(24.dp)

        Column {
            Text(
                text = "Username",
                color = Color(0xff0f2000),
                lineHeight = 1.33.em,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Space(4.dp)

            Text(
                text = "32 publications",
                color = Color(0xff0f2000),
                lineHeight = 1.43.em,
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = "Registered since 2024.04.01",
                color = Color(0xff0f2000),
                lineHeight = 1.43.em,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
