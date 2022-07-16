package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.FavouriteMealItemBinding
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.entities.Meal
import com.example.easyfood.entities.MealByCategory

class FavouriteMealsAdapter() :
    RecyclerView.Adapter<FavouriteMealsAdapter.FavouriteMealsViewHolder>() {

    class FavouriteMealsViewHolder(var binding: FavouriteMealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    lateinit var onItemClick: ((Meal) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMealsViewHolder {
        return FavouriteMealsViewHolder(
            FavouriteMealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouriteMealsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide
            .with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgFavMeal)
        holder.binding.tvFavMealName.text = meal.strMeal
        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}