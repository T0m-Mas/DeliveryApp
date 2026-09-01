package com.mrgndt.delivery.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrgndt.delivery.R
import com.mrgndt.delivery.model.Location
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@Composable
fun LocationInfoCard(
    modifier: Modifier = Modifier,
    location: Location,
    onClose: () -> Unit,
) {
    Column(
        modifier = modifier
            .shadow(
                8.dp,
                shape = RoundedCornerShape(32f)
            )
            .fillMaxWidth(0.9f)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(32f),
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = location.label ?: "Lugar sin nombre",
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onClose() },
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Cerrar",
                tint = MaterialTheme.colorScheme.onBackground

            )
        }

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            Text(
                text = location.address,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 18.sp
            )

        }
    }
}


@Preview
@Composable
fun LocationInfoCardPreview() {
    DeliveryAppTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LocationInfoCard(
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                Location(
                    id = 1,
                    latitude = 12.121212,
                    longitude = 12.12121,
                    address = "Casacuberta 1267, Castelar, Buenos Aires, Argentina",
                    label = "Casa de Tom"
                ),
                onClose = {}
            )
        }
    }
}
