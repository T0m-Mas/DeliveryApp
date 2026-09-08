package com.mrgndt.delivery.network.data

import kotlinx.serialization.Serializable

@Serializable
data class ComputeRouteBody(
    val origin: Point,
    val intermediates: List<Point>,
    val destination: Point,
    val travelMode: String = "DRIVE",
    val optimizeWaypointOrder: Boolean = true
)

@Serializable
data class Point(
    val location: Location
) {
    constructor(latitude: Double, longitude: Double) : this(
        location = Location(latLng = LatLng(latitude, longitude))
    )
}

///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////
// ComputeRouteResponse
///////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////

@Serializable
data class ComputeRouteResponse(
    val routes: List<Route>
)

@Serializable
data class Route(
    val legs: List<Leg>,
    val distanceMeters: Long,
    val duration: String,
    val staticDuration: String,
    val polyline: Polyline,
    val description: String,
    val viewport: Viewport,
    val travelAdvisory: Map<String, Any>,
    val optimizedIntermediateWaypointIndex: List<Long>,
    val localizedValues: LocalizedValues,
    val routeLabels: List<String>,
    val polylineDetails: Map<String, Any>,
)

@Serializable
data class Leg(
    val distanceMeters: Long,
    val duration: String,
    val staticDuration: String,
    val polyline: Polyline,
    val startLocation: Location,
    val endLocation: Location,
    val steps: List<Step>,
    val localizedValues: LocalizedValues,
)

@Serializable
data class Polyline(
    val encodedPolyline: String,
)

@Serializable
data class Location(
    val latLng: LatLng,
)

@Serializable
data class LatLng(
    val latitude: Double,
    val longitude: Double,
)

@Serializable
data class Step(
    val distanceMeters: Long,
    val staticDuration: String,
    val polyline: Polyline,
    val startLocation: Location,
    val endLocation: Location,
    val navigationInstruction: NavigationInstruction,
    val localizedValues: LocalizedValues,
    val travelMode: String,
)

@Serializable
data class NavigationInstruction(
    val maneuver: String,
    val instructions: String,
)

@Serializable
data class LocalizedValues(
    val distance: Distance,
    val staticDuration: StaticDuration,
)

@Serializable
data class Distance(
    val text: String,
)

@Serializable
data class StaticDuration(
    val text: String,
)

@Serializable
data class Viewport(
    val low: LatLng,
    val high: LatLng,
)
