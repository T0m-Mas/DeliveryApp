package com.mrgndt.delivery.model

import java.util.Date

data class Route(
    val id: Int,
    val stops: List<Stop>,
    val status: Status
) {
    data class Stop(
        val location: Location,
        val order: Int,
        val visited: Boolean,

    )

    enum class Status {
        Initialized,
        Started,
        Paused,
    }


}
