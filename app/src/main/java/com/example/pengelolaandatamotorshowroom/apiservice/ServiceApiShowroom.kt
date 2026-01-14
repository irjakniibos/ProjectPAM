package com.example.pengelolaandatamotorshowroom.apiservice

import com.example.pengelolaandatamotorshowroom.modeldata.*
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiShowroom {

    // Authentication
    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("logout.php")
    suspend fun logout(@Header("Authorization") token: String): ResponseApi<Unit>

    // Brand Management - DIPERBAIKI: ganti "merk" jadi "brand"
    @GET("brand/read.php")
    suspend fun getAllBrands(@Header("Authorization") token: String): BrandListResponse

    @POST("brand/create.php")
    suspend fun createBrand(
        @Header("Authorization") token: String,
        @Body request: BrandRequest
    ): ResponseApi<DataBrand>

    @PUT("brand/update.php")
    suspend fun updateBrand(
        @Header("Authorization") token: String,
        @Body request: BrandUpdateRequest
    ): ResponseApi<DataBrand>

    @DELETE("brand/delete.php")
    suspend fun deleteBrand(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): ResponseApi<Unit>

    // Motor Management
    @GET("motor/read.php")
    suspend fun getMotorsByBrand(
        @Header("Authorization") token: String,
        @Query("brand_id") brandId: Int
    ): MotorListResponse

    @GET("motor/read_detail.php")
    suspend fun getMotorDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): MotorDetailResponse

    @POST("motor/create.php")
    suspend fun createMotor(
        @Header("Authorization") token: String,
        @Body request: MotorRequest
    ): ResponseApi<DataMotor>

    @PUT("motor/update.php")
    suspend fun updateMotor(
        @Header("Authorization") token: String,
        @Body request: MotorUpdateRequest
    ): ResponseApi<DataMotor>

    @DELETE("motor/delete.php")
    suspend fun deleteMotor(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): ResponseApi<Unit>

    // Stock Management
    @PUT("stok/tambah.php")
    suspend fun tambahStok(
        @Header("Authorization") token: String,
        @Body request: StokRequest
    ): StokResponse

    @PUT("stok/kurangi.php")
    suspend fun kurangiStok(
        @Header("Authorization") token: String,
        @Body request: StokRequest
    ): StokResponse
}