package com.mrgndt.delivery.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.rememberCameraPositionState
import com.mrgndt.delivery.R
import com.mrgndt.delivery.ui.screen.home.component.DeliveryMap
import com.mrgndt.delivery.ui.screen.home.component.Drawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val sheetState = rememberModalBottomSheetState()

    val mapCameraPositionState = rememberCameraPositionState(
//        init = {
//            this.position = CameraPosition(
//                target = LatLng()
//            )
//        }
    )

    val scope = rememberCoroutineScope()

    var showSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = false,
            drawerContent = {
                Drawer(
                    onStartRouteClick = {
                        scope.launch {
                            drawerState.close()
                            showSheet = true
                            sheetState.show()
                        }
                    },
                    onNewLocationClick = {

                    },
                    onBack = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        ) {
            DeliveryMap(
                cameraPositionState = mapCameraPositionState
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
            AnimatedVisibility(showSheet) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        showSheet = false
                    }
                ) {
                    Column(
                        modifier = Modifier.height(400.dp)
                    ) {
                        Text(
                            text = "Sheet",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}