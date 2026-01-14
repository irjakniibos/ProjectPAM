package com.example.pengelolaandatamotorshowroom.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pengelolaandatamotorshowroom.ShowroomApplication
import com.example.pengelolaandatamotorshowroom.viewmodel.*

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShowroomApplication)
            LoginViewModel(application.container.repositoryAuth)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShowroomApplication)
            DashboardViewModel(application.container.repositoryBrand, application.container.repositoryAuth)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShowroomApplication)
            ListMotorViewModel(application.container.repositoryMotor)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShowroomApplication)
            DetailMotorViewModel(application.container.repositoryMotor)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShowroomApplication)
            FormMotorViewModel(application.container.repositoryMotor)
        }
    }
}