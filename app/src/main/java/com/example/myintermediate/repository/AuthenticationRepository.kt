package com.example.myintermediate.repository

import android.content.Context
import androidx.lifecycle.liveData
import com.example.myintermediate.Result
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.data.pref.UserModel
import com.example.myintermediate.data.pref.UserPreference
import com.example.myintermediate.data.remote.ApiService
import com.example.myintermediate.data.remote.ListStoryItem
import com.example.myintermediate.data.remote.RegisterResponse
import com.example.myintermediate.data.remote.ResponseLogin
import com.example.myintermediate.data.remote.ResponseStory
import com.example.myintermediate.di.Injection
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import kotlin.coroutines.coroutineContext

class AuthenticationRepository private constructor(
    private var apiService: ApiService, private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseLogin::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }


    fun getStory() = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getStories()
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getDetail(id: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getDetailStories(id)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit((errorResponse.message?.let { Result.Error(it) }))
        }
    }

    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(imageMultipart, requestBody)
            emit(Result.Success(successResponse))
        } catch (e : HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ResponseStory::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun update(apiService: ApiService) {
        this.apiService = apiService
    }

    companion object {
        @Volatile
        private var instance: AuthenticationRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: AuthenticationRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
