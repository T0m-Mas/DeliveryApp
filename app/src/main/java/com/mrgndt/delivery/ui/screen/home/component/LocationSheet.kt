package com.mrgndt.delivery.ui.screen.home.component

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.view.View.LAYOUT_DIRECTION_RTL
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.mrgndt.delivery.R
import com.mrgndt.delivery.ui.component.DeliveryAppAutoCompleteTextField
import com.mrgndt.delivery.ui.component.DeliveryAppTextField
import com.mrgndt.delivery.ui.component.SelectorItem
import com.mrgndt.delivery.ui.component.SquareButton
import com.mrgndt.delivery.ui.screen.home.LocationFormState
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSheet(
    modifier: Modifier = Modifier,
    locationFormState: LocationFormState,
    updateState: (LocationFormState) -> Unit,
    processAutoComplete: (String) -> Unit,
    onDismissRequest: () -> Unit,
    processSelectSuggestion: (String) -> Unit,
    saveLocation: () -> Unit,
) {

    fun resetFormAndDismiss() {
        updateState(LocationFormState())
        onDismissRequest()
    }

    val config = LocalConfiguration.current
    val isLandscape = config.orientation == ORIENTATION_LANDSCAPE
    val windowInsets = WindowInsets.safeDrawing.asPaddingValues()
    val layoutDirection = if (config.layoutDirection == LAYOUT_DIRECTION_RTL)
        LayoutDirection.Rtl else LayoutDirection.Ltr

    fun determinateTitle(): String {
        return if (locationFormState.canBeGhost) "Agregar Lugar"
        else if (locationFormState.isEditing) "Editar Lugar"
        else "Registrar Lugar"
    }

    fun toggleIsGhostLocation() {
        updateState(
            locationFormState.copy(isGhostLocation = !locationFormState.isGhostLocation)
        )
    }


    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = if (isLandscape) RoundedCornerShape(
                    topEnd = 30f,
                    bottomEnd = 30f
                ) else RoundedCornerShape(topStart = 30f, topEnd = 30f)
            )
            .height(500.dp)
            .widthIn(
                max = if (isLandscape) 500.dp else Dp.Unspecified
            )
            .then(
                if (isLandscape)
                    Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(start = windowInsets.calculateStartPadding(layoutDirection))
                        .scrollable(rememberScrollState(), Orientation.Vertical)
                else
                    Modifier.navigationBarsPadding()
            )

            .padding(top = 8.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = determinateTitle(),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600
            )
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { resetFormAndDismiss() },
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Cerrar",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        if (locationFormState.latLng == null) {
            DeliveryAppAutoCompleteTextField(
                value = locationFormState.address,
                onValueChange = {
                    updateState(
                        locationFormState.copy(
                            address = it
                        )
                    )
                    processAutoComplete(it)
                },
                label = "Ingrese una dirección o toque el mapa",
                placeholder = "Buscar por dirección",
                list = locationFormState.addressSuggestions.map { suggestions ->
                    SelectorItem(label = suggestions.label, value = suggestions.placeId)
                },
                singleLine = false,

                onValueSelected = {
                    processSelectSuggestion(it)
                }
            )
        } else {
            DeliveryAppTextField(
                value = locationFormState.label,
                onValueChange = {
                    updateState(
                        locationFormState.copy(
                            label = it
                        )
                    )
                },
                label = "Nombre",
                placeholder = "Sin Nombre"
            )
            DeliveryAppTextField(
                value = locationFormState.address,
                onValueChange = {
                    updateState(
                        locationFormState.copy(
                            address = it
                        )
                    )
                },
                singleLine = false,
                label = "Dirección",
                placeholder = "Ingrese una dirección"
            )
            if (locationFormState.canBeGhost) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { toggleIsGhostLocation() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Guardar Lugar",
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Switch(
                        checked = locationFormState.isGhostLocation.not(),
                        onCheckedChange = { toggleIsGhostLocation() }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SquareButton(
                modifier = Modifier.weight(1f),
                onClick = saveLocation,
                enabled = locationFormState.formIsValid
            ) {
                Text(
                    "Guardar"
                )
            }
        }
        BackHandler {
            resetFormAndDismiss()
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
                locationFormState = LocationFormState(
                    latLng = LatLng(0.0, 0.0),
                    canBeGhost = true
                ),
                updateState = {},
                onDismissRequest = {},
                processAutoComplete = {},
                processSelectSuggestion = {},
                saveLocation = {},
            )
        }
    }
}
