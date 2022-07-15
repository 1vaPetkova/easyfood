package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.CategoryTabItemBinding
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.entities.Category

class CategoriesTabAdapter() :
    RecyclerView.Adapter<CategoriesTabAdapter.CategoryTabViewHolder>() {
    inner class CategoryTabViewHolder(var binding: CategoryTabItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var categories = ArrayList<Category>()
    lateinit var onItemClick: ((Category) -> Unit)

    fun setCategories(categories: ArrayList<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryTabViewHolder {
        return CategoryTabViewHolder(
            CategoryTabItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryTabViewHolder, position: Int) {
        Glide
            .with(holder.itemView)
            .load(categories[position].strCategoryThumb)
            .into(holder.binding.imgCatTab)
        holder.binding.tvCatTabName.text = categories[position].strCategory
        holder.itemView.setOnClickListener {
            onItemClick.invoke(categories[position])
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}