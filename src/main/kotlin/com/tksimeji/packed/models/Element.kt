package com.tksimeji.packed.models

import com.tksimeji.packed.PackDirection
import com.tksimeji.packed.Vector3icSerializer
import com.tksimeji.packed.Vector4fcSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.joml.Vector3ic
import org.joml.Vector4fc

@Serializable
data class PackElement(
    @Serializable(with = Vector3icSerializer::class)
    val from: Vector3ic,
    @Serializable(with = Vector3icSerializer::class)
    val to: Vector3ic,
    val faces: Map<PackDirection, PackFace> = emptyMap(),
    val rotation: PackRotation? = null,
    val shade: Boolean? = null,
    val lightEmission: Int,
)

@Serializable
data class PackRotation(
    @Serializable(with = Vector3icSerializer::class)
    val origin: Vector3ic,
    val axis: PackAxis,
    val angle: Float,
    val rescale: Boolean? = null,
)

@Serializable
enum class PackAxis {
    @SerialName("x")
    X,

    @SerialName("y")
    Y,

    @SerialName("z")
    Z,
}

@Serializable
data class PackFace(
    @Serializable(with = Vector4fcSerializer::class)
    val uv: Vector4fc,
    val rotation: Int,
    val tintindex: Int? = null,
    val cullface: PackDirection? = null,
)
