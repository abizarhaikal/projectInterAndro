package com.example.myintermediate.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myintermediate.Result
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.data.pref.UserModel
import com.example.myintermediate.data.remote.ApiConfig
import com.example.myintermediate.databinding.FragmentLoginBinding
import com.example.myintermediate.di.Injection
import com.example.myintermediate.viewModel.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uploadData()
    }

    private fun uploadData() {
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text
        binding.loginButton.setOnClickListener {
            loginViewModel.login(email.toString(), password.toString())
                .observe(requireActivity()) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }

                            is Result.Success -> {
                                val token = result.data.loginResult.token
                                val username = result.data.loginResult.name
                                loginViewModel.saveSession(
                                    UserModel(
                                        email.toString(),
                                        token,
                                        username,
                                        true
                                    )
                                )
                                val userRepository = Injection.provideRepository(requireActivity())
                                showLoading(false)
                                userRepository.update(ApiConfig.getApiService(token))
                                MaterialAlertDialogBuilder(requireActivity())
                                    .setTitle("Login Success")
                                    .setMessage("Welcome ${result.data.loginResult.name}")
                                    .setPositiveButton("Ok") { _, _ ->
                                        val intent =
                                            Intent(requireActivity(), HomeActivity::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()
                                    }
                                    .show()
                            }

                            is Result.Error -> {
                                showToast(result.error)
                                showLoading(false)
                            }
                        }
                    }
                }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


}