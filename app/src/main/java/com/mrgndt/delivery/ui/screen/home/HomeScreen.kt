package com.mrgndt.delivery.ui.screen.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.location.Location
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View.LAYOUT_DIRECTION_LTR
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.google.maps.android.compose.rememberCameraPositionState
import com.mrgndt.delivery.R
import com.mrgndt.delivery.ui.component.Pin
import com.mrgndt.delivery.ui.component.PinColor
import com.mrgndt.delivery.ui.component.PinType
import com.mrgndt.delivery.ui.screen.home.component.DeliveryMap
import com.mrgndt.delivery.ui.screen.home.component.Drawer
import com.mrgndt.delivery.ui.screen.home.component.LocationConfirmDeleteDialog
import com.mrgndt.delivery.ui.screen.home.component.LocationDeleteNEditButtons
import com.mrgndt.delivery.ui.screen.home.component.LocationInfoCard
import com.mrgndt.delivery.ui.screen.home.component.LocationSheet
import com.mrgndt.delivery.ui.screen.home.component.RouteBottomFABsNText
import com.mrgndt.delivery.ui.screen.home.component.RouteSearchBar
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
    val routeFormState by viewModel.routeFormState.collectAsState()

    val statusBarPaddingValues = WindowInsets.statusBars.asPaddingValues()
    val navigationBarPaddingValues = WindowInsets.navigationBars.asPaddingValues()
    val safeDrawing = WindowInsets.safeDrawing.asPaddingValues()

    val context = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var locationPermissionsEnabled by remember { mutableStateOf(false) }

    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun triggerVibration(durationMs: Long) {
        // 2. Execute vibration using the safe API version
        if (vibrator.hasVibrator()) { // Safety check to see if hardware exists
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // For API 26 and above (OneShot effect)
                val effect =
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
            } else {
                // For API 25 and below (Deprecated in newer APIs but required for backward compatibility)
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        }
    }

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

    fun toggleSelectStop(location: com.mrgndt.delivery.model.Location) {
        if (routeFormState.stops.contains(location).not()) {
            triggerVibration(80)
        }
        viewModel.toggleSelectStop(location)
    }

    fun handleMapClick(latLng: LatLng) {
        when (state.mode) {
            HomeUiState.Mode.Idle -> Unit

            HomeUiState.Mode.LocationForm -> {
                viewModel.updateLocationFormLatLng(latLng = latLng)
            }

            HomeUiState.Mode.NewRoute -> Unit
            HomeUiState.Mode.Route -> Unit
        }
    }

    fun handleMapLongClick(latLng: LatLng) {
        when (state.mode) {
            HomeUiState.Mode.Idle -> {
                viewModel.updateMode(HomeUiState.Mode.LocationForm)
                viewModel.updateLocationFormState(
                    LocationFormState(latLng = latLng)
                )
            }

            HomeUiState.Mode.LocationForm -> {
                viewModel.updateLocationFormLatLng(latLng)
            }

            HomeUiState.Mode.NewRoute -> Unit
            HomeUiState.Mode.Route -> Unit
        }
    }

    fun handleLocationMarkerClick(location: com.mrgndt.delivery.model.Location) {
        if (state.mode == HomeUiState.Mode.Idle) {
            viewModel.setSelectedLocation(location)
        }
        if (state.mode == HomeUiState.Mode.NewRoute) {
            toggleSelectStop(location)
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

    LaunchedEffect(locationFormState.latLng, state.selectedLocation) {
        if (locationFormState.latLng != null) {
            updateMapCamera(
                locationFormState.latLng!!,
                if (mapCameraPositionState.position.zoom >= 16f)
                    mapCameraPositionState.position.zoom else 16f
            )
        } else if (state.selectedLocation != null) {
            updateMapCamera(
                LatLng(
                    state.selectedLocation!!.latitude,
                    state.selectedLocation!!.longitude,
                ),
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
                start = if (state.mode == HomeUiState.Mode.LocationForm) 500.dp
                else safeDrawing.calculateStartPadding(
                    if (layoutDirection == LAYOUT_DIRECTION_LTR)
                        LayoutDirection.Ltr else LayoutDirection.Rtl
                ) + 16.dp,
                end = 16.dp
            )
        } else {
            PaddingValues(
                top = statusBarPaddingValues.calculateTopPadding(),
                bottom = if (state.mode == HomeUiState.Mode.LocationForm) 500.dp
                else navigationBarPaddingValues.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
        }
    }

    fun determinePinColor(location: com.mrgndt.delivery.model.Location): PinColor {
        return if (state.mode == HomeUiState.Mode.NewRoute) {
            if (routeFormState.stops.contains(location)) {
                PinColor.Primary
            } else {
                PinColor.Secondary
            }
        } else {
            PinColor.Secondary
        }
    }

    fun determinePinType(location: com.mrgndt.delivery.model.Location): PinType {
        return if (state.mode == HomeUiState.Mode.NewRoute) {
            if (routeFormState.stops.contains(location)) {
                PinType.Check
            } else {
                PinType.Idle
            }
        } else {
            PinType.Idle
        }
    }


    fun shouldShowMenuButton(): Boolean {
        if (state.selectedLocation !== null) return false
        if (state.mode == HomeUiState.Mode.Idle) return true
        return false
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            Drawer(
                onStartRouteClick = {
                    scope.launch {
                        drawerState.close()
                        viewModel.updateMode(HomeUiState.Mode.NewRoute)
                    }
                },
                onNewLocationClick = {
                    scope.launch {
                        drawerState.close()
                        viewModel.updateMode(HomeUiState.Mode.LocationForm)
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
                gestureEnabled = state.selectedLocation == null,
                isMyLocationEnabled = locationPermissionsEnabled
            ) {
                //
                // Pins de LocationForm
                //
                if (locationFormState.latLng != null) {
                    Pin(
                        position = locationFormState.latLng!!,
                        color = PinColor.Primary
                    )
                }
                if (state.selectedLocation != null && locationFormState.latLng == null) {
                    Pin(
                        position = LatLng(
                            state.selectedLocation!!.latitude,
                            state.selectedLocation!!.longitude
                        ),
                        color = PinColor.Primary
                    )
                }
                //
                // Pins de Home
                //
                state.locations
                    .filter { loc ->
                        loc != state.selectedLocation
                    }
                    .forEach { location ->
                        key(
                            location.id,
                            location.latitude,
                            location.longitude,
                            determinePinColor(location),
                            determinePinType(location)
                        ) {
                            Pin(
                                position = LatLng(location.latitude, location.longitude),
                                onClick = {
                                    handleLocationMarkerClick(location)
                                },
                                color = determinePinColor(location),
                                type = determinePinType(location),
                                label = location.label ?: location.address
                            )
                        }
                    }

            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.TopStart),
                visible = shouldShowMenuButton(),
                enter = slideInHorizontally(initialOffsetX = { -it }),
                exit = slideOutHorizontally(targetOffsetX = { -it })
            ) {
                FloatingActionButton(
                    modifier = Modifier
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
            }
            // ==================================================
            // ==================================================
            // LOCATION SELECTED PSEUDO MODE
            // ==================================================
            // ==================================================
            if (state.selectedLocation != null && state.mode == HomeUiState.Mode.Idle) {

                LocationInfoCard(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .then(
                            if (isLandscape)
                                Modifier.statusBarsPadding() else Modifier
                                .statusBarsPadding()
                                .padding(16.dp)
                                .padding(top = 128.dp)
                        ),
                    location = state.selectedLocation!!,
                    onClose = {
                        viewModel.setSelectedLocation(null)
                        showDeleteConfirmationDialog = false
                    }
                )
                BackHandler {
                    viewModel.setSelectedLocation(null)
                    showDeleteConfirmationDialog = false
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .then(
                            if (isLandscape)
                                Modifier else Modifier.padding(bottom = 128.dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LocationDeleteNEditButtons(
                        onDeleteClick = {
                            showDeleteConfirmationDialog = true
                        },
                        onEditClick = {
                            viewModel.updateLocationFormState(
                                LocationFormState(
                                    id = state.selectedLocation!!.id,
                                    label = state.selectedLocation!!.label ?: "",
                                    address = state.selectedLocation!!.address,
                                    latLng = LatLng(
                                        state.selectedLocation!!.latitude,
                                        state.selectedLocation!!.longitude,
                                    ),
                                    isEditing = true
                                )
                            )
                            viewModel.updateMode(HomeUiState.Mode.LocationForm)
                        }
                    )
                }
                if (showDeleteConfirmationDialog) {
                    LocationConfirmDeleteDialog(
                        onConfirmation = {
                            viewModel.deleteSelectedLocation()
                            showDeleteConfirmationDialog = false
                        },
                        onDismissRequest = { showDeleteConfirmationDialog = false }
                    )
                }
            }
            // ==================================================
            // ==================================================
            // LOCATION FORM MODE
            // ==================================================
            // ==================================================
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomStart),
                visible = state.mode == HomeUiState.Mode.LocationForm,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                LocationSheet(
                    locationFormState = locationFormState,
                    updateState = { viewModel.updateLocationFormState(it) },
                    onDismissRequest = { viewModel.updateMode(HomeUiState.Mode.Idle) },
                    processAutoComplete = { viewModel.processAutoComplete(it) },
                    processSelectSuggestion = { viewModel.processSelectSuggestion(it) },
                    saveLocation = { viewModel.saveLocation() },
                )
            }

            // ==================================================
            // ==================================================
            // NEW ROUTE MODE -> STAGE 1: Seleccionar Paradas
            // ==================================================
            // ==================================================
            if (state.mode == HomeUiState.Mode.NewRoute && routeFormState.stage == RouteFormState.Stage.StopsSelection) {
                BackHandler {
                    viewModel.updateMode(HomeUiState.Mode.Idle)
                }
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = state.mode == HomeUiState.Mode.NewRoute && routeFormState.stage == RouteFormState.Stage.StopsSelection,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                RouteBottomFABsNText(
                    text = if (routeFormState.stops.isEmpty()) "Seleccioná las paradas"
                    else "${routeFormState.stops.size} seleccionada(s)",
                    onCheckClick = {},
                    onAddLocationClick = { viewModel.startNewOptionalLocationMode() }

                )
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.TopCenter),
                visible = state.mode == HomeUiState.Mode.NewRoute && routeFormState.stage == RouteFormState.Stage.StopsSelection,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                RouteSearchBar(
                    onSearch = {
                        viewModel.suggestLocations(it)
                    },
                    suggestions = routeFormState.stopsSuggestions,
                    onSuggestionClick = {
                        toggleSelectStop(it)
                        updateMapCamera(
                            LatLng(
                                it.latitude,
                                it.longitude,
                            ),
                            if (mapCameraPositionState.position.zoom >= 16f)
                                mapCameraPositionState.position.zoom else 16f
                        )
                    }
                )
            }
            // ==================================================
            // ==================================================
            // NEW ROUTE MODE -> STAGE 2: Seleccionar Largada y Llegada
            // ==================================================
            // ==================================================
            if (state.mode == HomeUiState.Mode.NewRoute && routeFormState.stage == RouteFormState.Stage.StartNEndSelection) {
                BackHandler {
                    viewModel
                }
            }

        }
    }

}
