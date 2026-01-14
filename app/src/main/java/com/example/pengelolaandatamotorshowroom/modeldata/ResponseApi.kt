package com.example.pengelolaandatamotorshowroom.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class ResponseApi<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

@Serializable
data class BrandListResponse(
    val success: Boolean,
    val data: List<DataBrand>
)

@Serializable
data class MotorListResponse(
    val success: Boolean,
    val data: List<DataMotor>
)

@Serializable
data class MotorDetailResponse(
    val success: Boolean,
    val data: DataMotor
)

@Serializable
data class StokResponse(
    val success: Boolean,
    val message: String,
    val data: StokData? = null
)

@Serializable
data class StokData(
    val motor_id: Int,
    val jumlah_stok: Int
)