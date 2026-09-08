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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.mrgndt.delivery.ui.component.SelectorItem
import com.mrgndt.delivery.ui.component.SquareButton
import com.mrgndt.delivery.ui.screen.home.RouteFormState
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteExtremePointeSheet(
    title: String,
    buttonText: String,
    formState: RouteFormState.ExtremePointFormState,
    updateState: (RouteFormState.ExtremePointFormState) -> Unit,
    onDismissRequest: () -> Unit,
    processAutoComplete: (String) -> Unit,
    processSelectSuggestion: (String) -> Unit,
    useMyLocationClick: () -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val config = LocalConfiguration.current
    val isLandscape = config.orientation == ORIENTATION_LANDSCAPE
    val windowInsets = WindowInsets.safeDrawing.asPaddingValues()
    val layoutDirection = if (config.layoutDirection == LAYOUT_DIRECTION_RTL)
        LayoutDirection.Rtl else LayoutDirection.Ltr

    Column(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = if (isLandscape) RoundedCornerShape(
                    topEnd = 30f,
                    bottomEnd = 30f
                ) else RoundedCornerShape(topStart = 30f, topEnd = 30f)
            )
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
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.W600
            )
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onDismissRequest() },
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Cerrar",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        if(formState.point == null){
            DeliveryAppAutoCompleteTextField(
                value = formState.address,
                onValueChange = {
                    updateState(
                        formState.copy(
                            address = it
                        )
                    )
                    processAutoComplete(it)
                },
                label = "Ingrese una dirección o toque el mapa",
                placeholder = "Buscar por dirección",
                list = formState.addressSuggestions.map { suggestions ->
                    SelectorItem(label = suggestions.label, value = suggestions.placeId)
                },
                singleLine = false,
                onValueSelected = {
                    processSelectSuggestion(it)
                }
            )
            SquareButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = useMyLocationClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_my_location),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Utilizar Ubicación Actual"
                )
            }
        }else{
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Punto seleccionado en el mapa",
                    color = MaterialTheme.colorScheme.onBackground,
                )
                TextButton(
                    onClick = {
                        updateState(
                            formState.copy(
                                point = null
                            )
                        )
                    }
                ) {
                    Text(
                        text = "Volver a buscar"
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
                onClick = submit,
                enabled = formState.isValid
            ) {
                Text(
                    buttonText
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
fun RouteExtremePointeSheetPreview() {
    DeliveryAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
        ) {
            RouteExtremePointeSheet(
                title = "Seleccionar Salida",
                buttonText = "Continuar",
                modifier = Modifier.align(Alignment.BottomCenter),
                formState = RouteFormState.ExtremePointFormState(
                    point = null
                ),
                updateState = {},
                onDismissRequest = {},
                submit = {},
                processAutoComplete = {},
                processSelectSuggestion = {},
                useMyLocationClick = {}
            )
        }
    }
}
