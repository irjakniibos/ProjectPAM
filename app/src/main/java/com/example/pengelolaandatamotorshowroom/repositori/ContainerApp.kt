package com.example.pengelolaandatamotorshowroom.repositori

import android.content.Context
import com.example.pengelolaandatamotorshowroom.apiservice.ServiceApiShowroom
import com.example.pengelolaandatamotorshowroom.local.UserPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

interface AppContainer {
    val repositoryAuth: RepositoryAuth
    val repositoryBrand: RepositoryBrand
    val repositoryMotor: RepositoryMotor
}

class ContainerApp(private val context: Context) : AppContainer {

    private val baseUrl = "http://10.0.2.2/showroom-backend/api/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val serviceApi: ServiceApiShowroom by lazy {
        retrofit.create(ServiceApiShowroom::class.java)
    }

    private val userPreferences: UserPreferences by lazy {
        UserPreferences(context)
    }

    override val repositoryAuth: RepositoryAuth by lazy {
        RepositoryAuth(serviceApi, userPreferences)
    }

    override val repositoryBrand: RepositoryBrand by lazy {
        RepositoryBrand(serviceApi, userPreferences)
    }

    override val repositoryMotor: RepositoryMotor by lazy {
        RepositoryMotor(serviceApi, userPreferences)
    }
}