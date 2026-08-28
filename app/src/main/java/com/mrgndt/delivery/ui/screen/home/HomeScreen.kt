package com.mrgndt.delivery.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.mrgndt.delivery.R
import com.mrgndt.delivery.ui.screen.home.component.DeliveryMap
import com.mrgndt.delivery.ui.screen.home.component.Drawer
import com.mrgndt.delivery.ui.screen.home.component.LocationSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val mapCameraPositionState = rememberCameraPositionState(
//        init = {
//            this.position = CameraPosition(
//                target = LatLng()
//            )
//        }
    )

    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()
    val locationFormState by viewModel.locationFormState.collectAsState()

    val statusBarPaddingValues = WindowInsets.statusBars.asPaddingValues()
    val navigationBarPaddingValues = WindowInsets.navigationBars.asPaddingValues()

    fun updateMapCamera(target: LatLng, zoom: Float, tilt: Float = 0f, bearing: Float = 0f) {
        val cameraUpdate = CameraUpdateFactory
            .newCameraPosition(
                CameraPosition(
                    target,
                    zoom,
                    tilt,
                    bearing
                )
            )
        scope.launch {
            mapCameraPositionState.animate(
                cameraUpdate
            )
        }
    }

    fun handleMapClick(latLng: LatLng) {
        when (state.mode) {
            HomeUiState.Mode.Idled -> {
                viewModel.updateMode(HomeUiState.Mode.NewLocation)
                viewModel.updateLocationFormState(
                    LocationFormState(latLng = latLng)
                )
                updateMapCamera(
                    latLng,
                    if (mapCameraPositionState.position.zoom >= 16f)
                        mapCameraPositionState.position.zoom else 16f
                )

            }

            HomeUiState.Mode.NewLocation -> {
                viewModel.updateLocationFormState(
                    LocationFormState(latLng = latLng)
                )
                updateMapCamera(
                    latLng,
                    if (mapCameraPositionState.position.zoom >= 16f)
                        mapCameraPositionState.position.zoom else 16f
                )
            }

            HomeUiState.Mode.NewRoute -> Unit
            HomeUiState.Mode.Route -> Unit
        }
    }

    fun handleMapLongClick(latLng: LatLng) {

    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            Drawer(
                onStartRouteClick = {
                    scope.launch {
                        drawerState.close()
                    }
                },
                onNewLocationClick = {
                    scope.launch {
                        drawerState.close()
                        viewModel.updateMode(HomeUiState.Mode.NewLocation)
                    }
                },
                onBack = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            DeliveryMap(
                cameraPositionState = mapCameraPositionState,
                onMapClick = ::handleMapClick,
                onMapLongClick = ::handleMapLongClick,
                contentPadding = PaddingValues(
                    top = statusBarPaddingValues.calculateTopPadding(),
                    bottom = if (state.mode == HomeUiState.Mode.NewLocation) 400.dp
                    else navigationBarPaddingValues.calculateBottomPadding()
                )
            )

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(16.dp),
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = "Menu"
                )
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = state.mode == HomeUiState.Mode.NewLocation,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                LocationSheet(
                    locationFormState = locationFormState,
                    updateState = { viewModel.updateLocationFormState(it) },
                    onDismissRequest = { viewModel.updateMode(HomeUiState.Mode.Idled) }
                )
            }
        }
    }

}
