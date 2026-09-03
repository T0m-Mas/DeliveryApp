package com.mrgndt.delivery.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    fillColor: Color = MaterialTheme.colorScheme.onBackground,
    outlineColor: Color = MaterialTheme.colorScheme.background,
    strokeWidth: Float = 6f,
    style: TextStyle = TextStyle.Default.copy(fontSize = 16.sp, fontWeight = FontWeight.Normal)
) {
    Box(modifier = modifier) {
        // 1. Background Layer: Draws the outline stroke
        Text(
            text = text,
            style = style.copy(
                color = outlineColor,
                drawStyle = Stroke(
                    width = strokeWidth,
                    join = StrokeJoin.Round
                )
            ),
            // Hide from accessibility services to prevent screen readers from reading it twice
            modifier = Modifier.semantics { hideFromAccessibility() }
        )

        // 2. Foreground Layer: Draws the filled text on top
        Text(
            text = text,
            color = fillColor,
            style = style
        )
    }
}

@Preview
@Composable
fun OutlinedTextPreview(){
    DeliveryAppTheme {
        OutlinedText("Hola mundo")
    }
}