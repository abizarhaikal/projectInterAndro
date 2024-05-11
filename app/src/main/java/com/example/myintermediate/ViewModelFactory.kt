package com.example.myintermediate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myintermediate.di.Injection
import com.example.myintermediate.repository.AuthenticationRepository
import com.example.myintermediate.viewModel.HomeFragmentViewModel
import com.example.myintermediate.viewModel.HomeViewModel
import com.example.myintermediate.viewModel.LoginViewModel
import com.example.myintermediate.viewModel.RegisterViewModel

class ViewModelFactory(private val repository : AuthenticationRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java) -> {
                HomeFragmentViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown View Model class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
