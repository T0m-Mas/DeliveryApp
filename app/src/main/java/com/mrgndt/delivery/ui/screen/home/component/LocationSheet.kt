package com.mrgndt.delivery.ui.screen.home.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrgndt.delivery.R
import com.mrgndt.delivery.ui.component.SquareButton
import com.mrgndt.delivery.ui.screen.home.LocationFormState
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSheet(
    modifier: Modifier = Modifier,
    locationFormState: LocationFormState,
    updateState: (LocationFormState) -> Unit,
    onDismissRequest: () -> Unit,
) {

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 30f, topEnd = 30f)
            )
            .height(400.dp)
            .navigationBarsPadding()

            .padding(top = 8.dp)
            .padding(16.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (locationFormState.isEditing) "Editar Lugar" else "Registrar Lugar",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600
            )
            Icon(
                modifier = Modifier.size(32.dp).clickable{ onDismissRequest() },
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Cerrar",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = locationFormState.label,
            onValueChange = {
                updateState(
                    locationFormState.copy(
                        label = it
                    )
                )
            },
            label = { Text("Nombre") },
            placeholder = { Text("Sin Nombre") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = locationFormState.address,
            onValueChange = {
                updateState(
                    locationFormState.copy(
                        address = it
                    )
                )
            },
            label = { Text("Dirección") },
            placeholder = { Text("Ingrese una dirección") }
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (locationFormState.isEditing) {
                SquareButton(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                    onClick = {}
                ) {
                    Text(
                        "Eliminar"
                    )
                }
            }
            SquareButton(
                modifier = Modifier.weight(1f),
                onClick = {}
            ) {
                Text(
                    "Guardar"
                )
            }
        }
        BackHandler {
            onDismissRequest()
        }
    }

}

@Preview
@Composable
fun LocationSheetPreview() {
    DeliveryAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
        ) {
            LocationSheet(
                modifier = Modifier.align(Alignment.BottomCenter),
                locationFormState = LocationFormState(),
                updateState = {},
                onDismissRequest = {},
            )
        }
    }
}
