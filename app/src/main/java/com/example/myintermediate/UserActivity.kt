package com.example.myintermediate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myintermediate.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding

    private var dataRequest: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupView()
    }

    private fun getExtra() {
        val intent = intent.getStringExtra(MainActivity.CODE_FRAGMENT)
        if (intent == "10") {
            setupView()
        } else if (intent == "20") {
            setupSignup()
        }
    }

    private fun setupView() {
        dataRequest = intent.getIntExtra(MainActivity.CODE_FRAGMENT ,10)
        if (dataRequest == 10) {
            val transaction = supportFragmentManager
            val loginFragment = LoginFragment()
            val fragment = transaction.findFragmentByTag(LoginFragment::class.java.simpleName)


            if (fragment !is LoginFragment) {
                Log.d("My LoginFragment" ,"Fragment Name :" + LoginFragment::class.java.simpleName)
                transaction
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container ,
                        loginFragment ,
                        LoginFragment::class.java.simpleName
                    )
                    .commit()
            }
        }
        else {
            val fragmentManager = supportFragmentManager
            val signupFragment = SignupFragment()
            val fragment = fragmentManager.findFragmentByTag(SignupFragment::class.java.simpleName)

            if (fragment !is SignupFragment) {
                fragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container ,
                        signupFragment ,
                        SignupFragment::class.java.simpleName
                    )
                    .commit()
            }
        }
    }

    private fun setupSignup() {
        val fragmentManager = supportFragmentManager
        val signupFragment = SignupFragment()
        val fragment = fragmentManager.findFragmentByTag(SignupFragment::class.java.simpleName)

        if (fragment !is SignupFragment) {
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container ,
                    signupFragment ,
                    SignupFragment::class.java.simpleName
                )
                .addToBackStack(null)
                .commit()
        }
    }
}