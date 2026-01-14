package com.example.pengelolaandatamotorshowroom.repositori

import com.example.pengelolaandatamotorshowroom.apiservice.ServiceApiShowroom
import com.example.pengelolaandatamotorshowroom.local.UserPreferences
import com.example.pengelolaandatamotorshowroom.modeldata.*

class RepositoryBrand(
    private val api: ServiceApiShowroom,
    private val userPreferences: UserPreferences
) {
    private fun getToken(): String {
        return userPreferences.getToken() ?: throw Exception("Token tidak ditemukan")
    }

    suspend fun getAllBrands(): List<DataBrand> {
        try {
            val token = getToken()
            val response = api.getAllBrands(token)
            if (response.success) {
                return response.data
            } else {
                throw Exception("Gagal mengambil data brand")
            }
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }

    suspend fun createBrand(namaBrand: String): ResponseApi<DataBrand> {
        try {
            val token = getToken()
            val response = api.createBrand(token, BrandRequest(nama_brand = namaBrand))
            if (!response.success) {
                throw Exception(response.message)
            }
            return response
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }

    suspend fun updateBrand(id: Int, namaBrand: String): ResponseApi<DataBrand> {
        try {
            val token = getToken()
            val response = api.updateBrand(token, BrandUpdateRequest(id = id, nama_brand = namaBrand))
            if (!response.success) {
                throw Exception(response.message)
            }
            return response
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }

    suspend fun deleteBrand(id: Int): ResponseApi<Unit> {
        try {
            val token = getToken()
            val response = api.deleteBrand(token, id)
            if (!response.success) {
                throw Exception(response.message)
            }
            return response
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }
}