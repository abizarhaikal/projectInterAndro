package com.example.myintermediate.viewModel

import androidx.lifecycle.ViewModel
import com.example.myintermediate.repository.AuthenticationRepository
import java.io.File

class UploadViewModel(private val repository: AuthenticationRepository): ViewModel() {

    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}