package com.mrgndt.delivery.ui.screen.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.location.Location
import android.view.View.LAYOUT_DIRECTION_LTR
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.mrgndt.delivery.R
import com.mrgndt.delivery.ui.screen.home.component.DeliveryMap
import com.mrgndt.delivery.ui.screen.home.component.Drawer
import com.mrgndt.delivery.ui.screen.home.component.LocationSheet
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val mapCameraPositionState = rememberCameraPositionState()

    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()
    val locationFormState by viewModel.locationFormState.collectAsState()

    val statusBarPaddingValues = WindowInsets.statusBars.asPaddingValues()
    val navigationBarPaddingValues = WindowInsets.navigationBars.asPaddingValues()
    val safeDrawing = WindowInsets.safeDrawing.asPaddingValues()

    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var locationPermissionsEnabled by remember { mutableStateOf(false) }

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

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (isGranted) {
            locationPermissionsEnabled = true
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    updateMapCamera(
                        target = LatLng(location.latitude, location.longitude),
                        zoom = 12f
                    )
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, "${exception.message}", Toast.LENGTH_SHORT).show()

            }
        } else {
            locationPermissionsEnabled = false
        }
    }


    fun handleMapClick(latLng: LatLng) {
        when (state.mode) {
            HomeUiState.Mode.Idled -> Unit

            HomeUiState.Mode.NewLocation -> {
                viewModel.updateLocationFormState(
                    LocationFormState(latLng = latLng)
                )
            }

            HomeUiState.Mode.NewRoute -> Unit
            HomeUiState.Mode.Route -> Unit
        }
    }

    fun handleMapLongClick(latLng: LatLng) {
        when (state.mode) {
            HomeUiState.Mode.Idled -> {
                viewModel.updateMode(HomeUiState.Mode.NewLocation)
                viewModel.updateLocationFormState(
                    LocationFormState(latLng = latLng)
                )
            }

            HomeUiState.Mode.NewLocation -> {
                viewModel.updateLocationFormState(
                    LocationFormState(latLng = latLng)
                )
            }

            HomeUiState.Mode.NewRoute -> Unit
            HomeUiState.Mode.Route -> Unit
        }
    }


    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(locationFormState.latLng) {
        if (locationFormState.latLng != null) {
            updateMapCamera(
                locationFormState.latLng!!,
                if (mapCameraPositionState.position.zoom >= 16f)
                    mapCameraPositionState.position.zoom else 16f
            )
        }
    }


    val config = LocalConfiguration.current
    val isLandscape = config.orientation == ORIENTATION_LANDSCAPE
    val layoutDirection = config.layoutDirection

    fun determinePaddingValuesForMap(): PaddingValues {
        return if (isLandscape) {
            PaddingValues(
                top = statusBarPaddingValues.calculateTopPadding(),
                bottom = navigationBarPaddingValues.calculateBottomPadding(),
                start = if (state.mode == HomeUiState.Mode.NewLocation) 500.dp
                else safeDrawing.calculateStartPadding(
                    if (layoutDirection == LAYOUT_DIRECTION_LTR)
                        LayoutDirection.Ltr else LayoutDirection.Rtl
                ) + 16.dp,
                end = 16.dp
            )
        } else {
            PaddingValues(
                top = statusBarPaddingValues.calculateTopPadding(),
                bottom = if (state.mode == HomeUiState.Mode.NewLocation) 500.dp
                else navigationBarPaddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
        }
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
                contentPadding = determinePaddingValuesForMap(),
                isMyLocationEnabled = locationPermissionsEnabled
            ) {
                if (locationFormState.latLng != null) {
                    Marker(
                        state = rememberUpdatedMarkerState(position = locationFormState.latLng!!)
                    )
                }

                state.locations.forEach { location ->
                    key(location.id) {
                        Marker(
                            state = rememberUpdatedMarkerState(
                                position = LatLng(location.latitude, location.longitude)
                            ),
                            title = location.label ?: location.address,
                            snippet = location.address
                        )
                    }
                }

            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .safeDrawingPadding()
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
                modifier = Modifier.align(Alignment.BottomStart),
                visible = state.mode == HomeUiState.Mode.NewLocation,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                LocationSheet(
                    locationFormState = locationFormState,
                    updateState = { viewModel.updateLocationFormState(it) },
                    onDismissRequest = { viewModel.updateMode(HomeUiState.Mode.Idled) },
                    processAutoComplete = { viewModel.processAutoComplete(it) },
                    processSelectSuggestion = { viewModel.processSelectSuggestion(it) },
                    saveLocation = { viewModel.saveLocation() },
                    deleteLocation = {}
                )
            }
        }
    }

}
