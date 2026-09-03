package com.mrgndt.delivery.ui.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrgndt.delivery.R
import com.mrgndt.delivery.ui.component.OutlinedText
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@Composable
fun RouteBottomFABsNText(
    text:String,
    onCheckClick: () -> Unit,
    onAddLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .padding(16.dp)
            .padding(bottom = 16.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedText(
            text = text,
            strokeWidth = 16f
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                modifier = Modifier.size(40.dp),
                onClick = onAddLocationClick
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.ic_location_add),
                    contentDescription = null
                )
            }
            FloatingActionButton(
                modifier = Modifier.size(80.dp),
                onClick = onCheckClick
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun BottonFABsNTextPreview(){

    DeliveryAppTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            RouteBottomFABsNText(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "Selecciona las paradas",
                onCheckClick = {},
                onAddLocationClick = {}
            )
        }
    }


}