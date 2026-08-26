package com.mrgndt.delivery.model

data class Location(
    val id:Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val label: String? = null,
)
