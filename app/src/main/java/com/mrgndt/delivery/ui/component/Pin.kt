package com.mrgndt.delivery.ui.component

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.mrgndt.delivery.R

enum class PinColor {
    Primary, Secondary, Disabled
}

enum class PinType {
    Idle, Start, Stop
}

@Composable
@GoogleMapComposable
fun Pin(
    position: LatLng,
    onClick: () -> Unit = {},
    color: PinColor = PinColor.Primary,
    type: PinType = PinType.Idle
) {

    val isSystemDarkTheme = isSystemInDarkTheme()

    val context = LocalContext.current

    val pinResourceId: Int = if (!isSystemDarkTheme) {
        when (color) {
            PinColor.Primary -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_primary_light
                    PinType.Start -> R.drawable.pin_primary_light_start
                    PinType.Stop -> R.drawable.pin_primary_light_stop

                }
            }

            PinColor.Secondary -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_secondary_light
                    PinType.Start -> R.drawable.pin_secondary_light_start
                    PinType.Stop -> R.drawable.pin_secondary_light_stop

                }
            }

            PinColor.Disabled -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_disabled_light
                    PinType.Start -> R.drawable.pin_disabled_light_start
                    PinType.Stop -> R.drawable.pin_disabled_light_stop

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

                }
            }

            PinColor.Secondary -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_secondary_dark
                    PinType.Start -> R.drawable.pin_secondary_dark_start
                    PinType.Stop -> R.drawable.pin_secondary_dark_stop

                }
            }

            PinColor.Disabled -> {
                when (type) {
                    PinType.Idle -> R.drawable.pin_disabled_dark
                    PinType.Start -> R.drawable.pin_disabled_dark_start
                    PinType.Stop -> R.drawable.pin_disabled_dark_stop

                }
            }
        }
    }


    Marker(
        state = rememberUpdatedMarkerState(
            position = position
        ),
        onClick = {
            onClick()
            false
        },
        icon = bitmapDescriptorFromVector(context, pinResourceId)
    )
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
