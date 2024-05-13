package com.example.myintermediate.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
            }
        }

        val homeFragment = HomeFragment()
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Logout")
                setMessage("Are you sure want to logout?")
                setPositiveButton("Yes") { _, _ ->
                    viewModel.logout()
                    finish()
                    supportFragmentManager.beginTransaction().remove(homeFragment).commit()
                    startActivity(Intent(this@HomeActivity ,MainActivity::class.java))
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
//            viewModel.logout()
//            finish()
//            supportFragmentManager.beginTransaction().remove(homeFragment).commit()
//            startActivity(Intent(this@HomeActivity ,MainActivity::class.java))
        }

        val navView: BottomNavigationView = binding.navView

        val navContrroller = findNavController(R.id.nav_host_fragment_activity_home)

        navView.setupWithNavController(navContrroller)
    }

}