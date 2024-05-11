package com.example.myintermediate.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myintermediate.R
import com.example.myintermediate.ViewModelFactory
import com.example.myintermediate.databinding.ActivityHomeBinding
import com.example.myintermediate.viewModel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class
HomeActivity : AppCompatActivity() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getSession().observe(this) { session ->
            if (!session.isLogin) {
                finish()
                startActivity(Intent(this@HomeActivity ,MainActivity::class.java))
            } else {
                val username = session.username
                val bundle = Bundle()
                bundle.putString(HomeFragment.EXTRA_NAME, username)

                Log.d("HomeActivity", "this name is $username")

                val homeFragment = HomeFragment()
                homeFragment.arguments = bundle
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navContrroller = findNavController(R.id.nav_host_fragment_activity_home)

        navView.setupWithNavController(navContrroller)


    }

}