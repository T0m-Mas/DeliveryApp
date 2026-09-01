package com.mrgndt.delivery.ui.screen.home.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DefaultMapContentPadding
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.mrgndt.delivery.R

@Composable
fun DeliveryMap(
    cameraPositionState: CameraPositionState,
    onMapClick: ((LatLng) -> Unit)? = null,
    onMapLongClick: ((LatLng) -> Unit)? = null,
    contentPadding: PaddingValues = DefaultMapContentPadding,
    isMyLocationEnabled: Boolean = false,
    gestureEnabled: Boolean = true,
    content: @Composable @GoogleMapComposable () -> Unit,
) {

    val context = LocalContext.current

    val isDark = isSystemInDarkTheme()


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            compassEnabled = false,
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false,
            rotationGesturesEnabled = false,
            scrollGesturesEnabled = gestureEnabled,
            scrollGesturesEnabledDuringRotateOrZoom = gestureEnabled,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = gestureEnabled
        ),
        contentPadding = contentPadding,
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                context,
                if (isDark) R.raw.map_dark else R.raw.map_light
            ),
            isMyLocationEnabled = isMyLocationEnabled
        ),
        onMapClick = onMapClick,
        onMapLongClick = onMapLongClick,
    ) {
        content()
    }
}
