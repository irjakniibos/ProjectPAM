package com.example.pengelolaandatamotorshowroom

import android.app.Application
import com.example.pengelolaandatamotorshowroom.repositori.AppContainer
import com.example.pengelolaandatamotorshowroom.repositori.ContainerApp

class ShowroomApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = ContainerApp(this)
    }
}