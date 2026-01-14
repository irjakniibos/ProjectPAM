package com.example.pengelolaandatamotorshowroom.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class DataMotor(
    val id: Int,
    val nama_motor: String,
    val brand_id: Int,
    val nama_brand: String,
    val tipe: String,
    val tahun: Int,
    val harga: Double,
    val warna: String,
    val jumlah_stok: Int,
    val created_at: String
)

@Serializable
data class MotorRequest(
    val nama_motor: String,
    val brand_id: Int,
    val tipe: String,
    val tahun: Int,
    val harga: Double,
    val warna: String,
    val stok_awal: Int
)

@Serializable
data class MotorUpdateRequest(
    val id: Int,
    val nama_motor: String,
    val brand_id: Int,
    val tipe: String,
    val tahun: Int,
    val harga: Double,
    val warna: String
)

@Serializable
data class StokRequest(
    val motor_id: Int
)