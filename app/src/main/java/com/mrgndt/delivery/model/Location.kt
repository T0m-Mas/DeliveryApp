package com.mrgndt.delivery.model

data class Location(
    val id: Long = 0L,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val label: String? = null,
)
