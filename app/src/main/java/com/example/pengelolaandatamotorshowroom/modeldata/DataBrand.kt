package com.example.pengelolaandatamotorshowroom.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class DataBrand(
    val id: Int,
    val nama_brand: String,
    val created_at: String
)

@Serializable
data class BrandRequest(
    val nama_brand: String
)

@Serializable
data class BrandUpdateRequest(
    val id: Int,
    val nama_brand: String
)