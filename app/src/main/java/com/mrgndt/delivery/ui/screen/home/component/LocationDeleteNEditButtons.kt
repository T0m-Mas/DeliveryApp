package com.mrgndt.delivery.ui.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrgndt.delivery.R
import com.mrgndt.delivery.ui.component.CircularButton
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@Composable
fun LocationDeleteNEditButtons(
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    CircularButton(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
        onClick = onDeleteClick
    ) {
        Icon(
            modifier = Modifier.size(56.dp),
            painter = painterResource(R.drawable.ic_trash),
            contentDescription = null,
        )
    }
    CircularButton(
        onClick = onEditClick
    ) {
        Icon(
            modifier = Modifier.size(56.dp),
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
fun ButtonsPreview() {
    DeliveryAppTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LocationDeleteNEditButtons(
                    {},{}
                )
            }

        }
    }
}