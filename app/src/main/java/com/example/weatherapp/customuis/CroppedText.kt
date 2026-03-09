package com.example.weatherapp.customuis

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Composable
fun CroppedText(
    text: String,
    topCrop: Dp,
    bottomCrop: Dp,
    style: TextStyle
) {
    Box(modifier = Modifier) {
        Text(
            text = text,
            style = style,
            modifier = Modifier.layout { measurable, constraints ->

                val placeable = measurable.measure(constraints)

                val top = topCrop.roundToPx()
                val bottom = bottomCrop.roundToPx()

                layout(placeable.width, placeable.height - top - bottom) {
                    placeable.place(0, -top)
                }
            }
        )
    }
}