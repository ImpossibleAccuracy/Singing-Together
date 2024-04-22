package org.singing.app.ui.screens.record.views.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp


@Composable
fun RecordHistory(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = Color(0xfff1f4f9))
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Text(
            text = "Запись",
            color = Color(0xff181c20),
            textAlign = TextAlign.Center,
            lineHeight = 1.5.em,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp
            ),
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(color = Color(0xffe5e8ed))
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
            ) {
                item {
                    Text(
                        text = "00:03",
                        color = Color(0xff41474d),
                        textAlign = TextAlign.Center,
                        lineHeight = 1.33.em,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "C#4",
                            color = Color(0xff181c20),
                            lineHeight = 1.5.em,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.15.sp
                            ),
                        )

                        Text(
                            text = "(400Hz)",
                            color = Color(0xff181c20),
                            textAlign = TextAlign.Center,
                            lineHeight = 1.5.em,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.15.sp
                            ),
                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = "C4",
                            color = Color(0xff181c20),
                            textAlign = TextAlign.End,
                            lineHeight = 1.43.em,
                            style = MaterialTheme.typography.labelLarge,
                        )

                        Text(
                            text = "(410Hz)",
                            color = Color(0xff181c20),
                            textAlign = TextAlign.End,
                            lineHeight = 1.43.em,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}
