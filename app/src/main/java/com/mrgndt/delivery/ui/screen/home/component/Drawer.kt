package com.mrgndt.delivery.ui.screen.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrgndt.delivery.R

@Composable
fun Drawer(
    onStartRouteClick: ()->Unit,
    onNewLocationClick: ()->Unit,
) {
    ModalDrawerSheet(
        modifier = Modifier,
        ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(
                    horizontal = 16.dp,
                    vertical = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DrawerItem(
                label = { Text("Iniciar Recorrido") },
                icon = { Icon(painterResource(R.drawable.ic_route), null) },
                onClick = onStartRouteClick
            )
            DrawerItem(
                label = { Text("Guardar Ubicación") },
                icon = { Icon(painterResource(R.drawable.ic_location_add), null) },
                onClick = onNewLocationClick
            )
        }
    }
}

@Composable
fun DrawerItem(
    label: @Composable (() -> Unit),
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.invoke()
        label()
    }
}

@Preview
@Composable
fun DrawerPreview() {
    ModalNavigationDrawer(
        drawerState = rememberDrawerState(DrawerValue.Open),
        drawerContent = {
            Drawer(
                onStartRouteClick = {},
                onNewLocationClick = {}
            )
        }
    ) {}
}