package net.azisaba.packed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PackDyeColor {
    @SerialName("white")
    WHITE,

    @SerialName("orange")
    ORANGE,

    @SerialName("magenta")
    MAGENTA,

    @SerialName("light_blue")
    LIGHT_BLUE,

    @SerialName("yellow")
    YELLOW,

    @SerialName("lime")
    LIME,

    @SerialName("pink")
    PINK,

    @SerialName("gray")
    GRAY,

    @SerialName("light_gray")
    LIGHT_GRAY,

    @SerialName("cyan")
    CYAN,

    @SerialName("purple")
    PURPLE,

    @SerialName("blue")
    BLUE,

    @SerialName("brown")
    BROWN,

    @SerialName("green")
    GREEN,

    @SerialName("red")
    RED,

    @SerialName("black")
    BLACK,
}
