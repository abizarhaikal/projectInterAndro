package com.example.myintermediate.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myintermediate.R
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.databinding.ActivityHomeBinding
import com.example.myintermediate.viewModel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setupView()
        viewModel.getSession().observe(this) {session ->
            if (!session.isLogin) {
                finish()
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            }
            else {
                val intent = intent.getStringExtra("username")
                binding.user.text = intent
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

//    private fun setupView() {
//        viewModel.getSession().observe(this ) {session ->
//            if (!session.isLogin) {
//                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
//                finish()
//            }
//        }
//    }
}