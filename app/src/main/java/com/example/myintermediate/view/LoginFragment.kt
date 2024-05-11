package com.example.myintermediate.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myintermediate.Result
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.data.pref.UserModel
import com.example.myintermediate.data.remote.ApiConfig
import com.example.myintermediate.databinding.FragmentLoginBinding
import com.example.myintermediate.di.Injection
import com.example.myintermediate.viewModel.LoginViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>() {
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
                                showToast("Lagi Loading ya!!")
                            }

                            is Result.Success -> {
                                val token = result.data.loginResult.token
                                val username = result.data.loginResult.name
                                loginViewModel.saveSession(UserModel(email.toString(), token, username,true))
                                val userRepository = Injection.provideRepository(requireActivity())
                                userRepository.update(ApiConfig.getApiService(token))
                                AlertDialog.Builder(requireActivity()).apply {
                                    setTitle("Login Success")
                                    setMessage("Welcome ${result.data.loginResult.name}")
                                    setPositiveButton("Ok") { _, _ ->
                                        val intent = Intent(requireActivity(), HomeActivity::class.java)

                                        startActivity(intent)

                                        val name = result.data.loginResult.name

//                                        val homeFragment = HomeFragment()
//                                        val bundle = Bundle()
//                                        bundle.putString(HomeFragment.EXTRA_NAME, name)
//                                        homeFragment.arguments = bundle
//                                        homeFragment.name = name
                                    }.create()
                                    show()
                                }
                            }

                            is Result.Error -> {
                                showToast(result.error)
                            }
                        }
                    }
                }
        }
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