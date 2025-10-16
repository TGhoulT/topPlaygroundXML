package com.example.topplaygroundxml.presentation.carlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.topplaygroundxml.databinding.ItemCarBinding
import com.example.topplaygroundxml.domain.model.Car

class CarAdapter(
    private var carList: List<Car>
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(
        private val binding: ItemCarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) {
            binding.brandModelText.text = "${car.brand} ${car.model}"
            binding.yearText.text = "Год: ${car.year}"
            binding.descriptionText.text = car.description
            binding.priceText.text = "Цена: ${car.cost} ₽"
            binding.carImage.setImageResource(car.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(carList[position])
    }

    override fun getItemCount(): Int = carList.size

}