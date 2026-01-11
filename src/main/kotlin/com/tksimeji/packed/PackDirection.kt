package com.tksimeji.packed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PackDirection {
    @SerialName("down")
    DOWN,

    @SerialName("up")
    UP,

    @SerialName("north")
    NORTH,

    @SerialName("south")
    SOUTH,

    @SerialName("west")
    WEST,

    @SerialName("east")
    EAST,
}