package com.mrgndt.delivery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrgndt.delivery.ui.screen.home.HomeScreen
import com.mrgndt.delivery.ui.screen.home.HomeViewModel
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

class MainActivity : ComponentActivity() {

    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            homeViewModel = viewModel(factory = HomeViewModel.Factory)
            DeliveryAppTheme {
                HomeScreen(
                    viewModel = homeViewModel
                )
            }
        }
    }
}
