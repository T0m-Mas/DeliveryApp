package com.mrgndt.delivery.ui.screen.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mrgndt.delivery.R
import com.mrgndt.delivery.model.Location
import com.mrgndt.delivery.ui.theme.DeliveryAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    suggestions: List<Location> = emptyList(),
    onSuggestionClick: (Location)->Unit,
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
                onQueryChange = {
                    query = it
                    onSearch(it)
                },
                onSearch = {
                    onSearch(it)
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text("Buscar lugares guardados")
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        ) {
        // Display search results in a scrollable column
        Column(Modifier.verticalScroll(rememberScrollState())) {
            suggestions.forEach { location ->
                ListItem(
                    headlineContent = {
                        Text(location.label ?: "Lugar Sin Nombre")
                    },
                    supportingContent = {
                        Text(location.address)
                    },
                    leadingContent = {
                        Icon(
                            painter = painterResource(R.drawable.ic_location),
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier
                        .clickable {
                            onSuggestionClick(location)
                            query = ""
                            expanded = false
                        }
                        .fillMaxWidth()
                )
            }
        }

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
                onSearch = {},
                onSuggestionClick={}
            )
        }
    }
}