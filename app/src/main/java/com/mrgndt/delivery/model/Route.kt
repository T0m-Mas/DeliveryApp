package com.mrgndt.delivery.model

import java.util.Date

data class Route(
    val id: Int,
    val stops: List<Stop>
) {
    data class Stop(
        val location: Location,
        val order: Int,
        val date: Date,
    )
}
