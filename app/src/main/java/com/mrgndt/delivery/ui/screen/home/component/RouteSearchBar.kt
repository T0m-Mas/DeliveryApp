package com.mrgndt.delivery.ui.screen.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
) {

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    var query by rememberSaveable {
        mutableStateOf("")
    }


    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    onSearch(it)
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text("Search")
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },


        ) {

    }
}

@Preview
@Composable
fun RouteSearchBarPreview() {
    DeliveryAppTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            RouteSearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                onSearch = {}
            )
        }
    }
}