package com.mrgndt.delivery.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.mrgndt.delivery.R

enum class PinColor {
    Primary, Secondary, Disabled
}

enum class PinType {
    Idle, Start, Stop, Check
}

@Composable
@GoogleMapComposable
fun Pin(
    position: LatLng,
    onClick: () -> Unit = {},
    color: PinColor = PinColor.Primary,
    type: PinType = PinType.Idle,
    label: String? = null,
) {

    val isSystemDarkTheme = isSystemInDarkTheme()

    val pinResourceId: Int = if (!isSystemDarkTheme) {
        when (color) {
            PinColor.Primary -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_primary_light
                    PinType.Start -> R.drawable.pin_primary_light_start
                    PinType.Stop -> R.drawable.pin_primary_light_stop
                    PinType.Check -> R.drawable.pin_primary_light_check

                }
            }

            PinColor.Secondary -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_secondary_light
                    PinType.Start -> R.drawable.pin_secondary_light_start
                    PinType.Stop -> R.drawable.pin_secondary_light_stop
                    PinType.Check -> R.drawable.pin_secondary_light_check

                }
            }

            PinColor.Disabled -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_disabled_light
                    PinType.Start -> R.drawable.pin_disabled_light_start
                    PinType.Stop -> R.drawable.pin_disabled_light_stop
                    PinType.Check -> R.drawable.pin_disabled_light_check

                }
            }
        }
    } else {
        when (color) {
            PinColor.Primary -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_primary_dark
                    PinType.Start -> R.drawable.pin_primary_dark_start
                    PinType.Stop -> R.drawable.pin_primary_dark_stop
                    PinType.Check -> R.drawable.pin_primary_dark_check

                }
            }

            PinColor.Secondary -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_secondary_dark
                    PinType.Start -> R.drawable.pin_secondary_dark_start
                    PinType.Stop -> R.drawable.pin_secondary_dark_stop
                    PinType.Check -> R.drawable.pin_secondary_dark_check

                }
            }

            PinColor.Disabled -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_disabled_dark
                    PinType.Start -> R.drawable.pin_disabled_dark_start
                    PinType.Stop -> R.drawable.pin_disabled_dark_stop
                    PinType.Check -> R.drawable.pin_disabled_dark_check

                }
            }
        }
    }


    MarkerComposable(
        state = rememberUpdatedMarkerState(
            position = position
        ),
        onClick = {
            onClick()
            false
        },
        anchor = if (label != null) Offset(0.172f, 1.0f) else Offset(0.5f, 1.0f)
    ) {
        Row(
            modifier = Modifier
                .then(
                    other =
                        if (label != null)
                            Modifier.width(120.dp)
                        else Modifier
                ),
            verticalAlignment = Alignment.Top,
        ) {
            Image(
                modifier = Modifier.size(42.dp),
                painter = painterResource(pinResourceId),
                contentDescription = null
            )
            if (label != null) {
                OutlinedText(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = label,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 10.sp
                    )
                )
            }

        }
    }
}

@Preview
@Composable
fun Preview(){

    var label:String? = "ADOX S.A."

    Box(){
    Box(Modifier.padding(start = (120*0.172).dp,top=42.dp).size(1.dp).background(Color.Red))
    Row(
        modifier = Modifier
            .then(
                other =
                    if (label != null)
                        Modifier.width(120.dp)
                    else Modifier
            ),
        verticalAlignment = Alignment.Top,
    ) {
        Image(
            modifier = Modifier.size(42.dp),
            painter = painterResource(R.drawable.pin_primary_light),
            contentDescription = null
        )
        if (label != null) {
            OutlinedText(
                modifier = Modifier.padding(vertical = 8.dp),
                text = label,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 10.sp
                )
            )
        }

    }
    }
}
