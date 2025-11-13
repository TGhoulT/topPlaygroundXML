package com.example.topplaygroundxml.features.carlist.presentation.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topplaygroundxml.R
import com.example.topplaygroundxml.databinding.ActivitySecondBinding
import com.example.topplaygroundxml.features.carlist.domain.model.Car

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val carList = listOf(
            Car(
                "Sybau",
                "Camry",
                2020,
                "Комфортный седан бизнес-класса",
                2000000,
                R.drawable.sybau
            ),
            Car(
                "Honda",
                "Civic",
                2019,
                "Классический городской седан",
                1800000,
                R.drawable.honda_civic
            ),
            Car("BMW", "X5", 2021, "Премиальный внедорожник", 5000000, R.drawable.bmw_x5),
            Car("Toyota", "RAV4", 2018, "Награда за годовщину", 2500000, R.drawable.toyota_rav_4),
            Car(
                "Mercedes",
                "C-Class (Piggy)",
                2022,
                "Роскошный бизнес-седан",
                4500000,
                R.drawable.mercedes_piggy
            ),
            Car(
                "Toyota",
                "Corolla",
                1989,
                "Нишевая легенда автомобилей",
                3500000,
                R.drawable.toyota_corolla_1989
            ),
            Car(
                "Lada",
                "Klyuchik",
                2023,
                "Надёжный отечественный автомобиль",
                1200000,
                R.drawable.lada_klyuchik
            )
        )

        val adapter = CarAdapter(carList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}