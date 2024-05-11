package com.example.myintermediate.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.example.myintermediate.R
import com.example.myintermediate.Result
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.databinding.FragmentSignupBinding
import com.example.myintermediate.viewModel.RegisterViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentSignupBinding
    private val signupViewModel by viewModels<RegisterViewModel>() {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uploadData()
    }

    private fun uploadData() {
        val nama = binding.nameEditText.text
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text
        binding.signupButton.setOnClickListener {
            signupViewModel.register(
                nama.toString(), email.toString(),
                password.toString()
            ).observe(requireActivity()) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showToast("Lagi Loading ya!!")
                        }

                        is Result.Success -> {
                            AlertDialog.Builder(requireActivity()).apply {
                                setTitle("Berhasil")
                                setMessage("Akun Created")
                                setPositiveButton("Lanjut") { _, _ ->
                                    requireActivity().finish()
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }


}