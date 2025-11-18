package com.example.topplaygroundxml.features.main.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ждем, когда view будет готово
        binding.root.post {
            setupNavigation()
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Настройка BottomNavigationView (без ActionBar)
        binding.bottomNavigationView.setupWithNavController(navController)

        // Скрывать BottomNavigationView на экранах авторизации
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registrationFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    // Скрываем ActionBar если он есть
                    supportActionBar?.hide()
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    // Показываем ActionBar если он есть
                    supportActionBar?.show()
                }
            }
        }
    }
}