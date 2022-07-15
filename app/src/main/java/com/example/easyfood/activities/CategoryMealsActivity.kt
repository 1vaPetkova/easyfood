package com.example.easyfood.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.adapters.MealsAdapter
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.entities.MealByCategory
import com.example.easyfood.utils.Constants
import com.example.easyfood.utils.Constants.Companion.CATEGORY_DESCRIPTION
import com.example.easyfood.utils.Constants.Companion.CATEGORY_NAME
import com.example.easyfood.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var categoryName: String
    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var mealsAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        getCategoryInformationFromIntent()
        categoryMealsViewModel.getCategoryMeals(categoryName)
        //Meals RecyclerView
        mealsAdapter = MealsAdapter()
        prepareCategoriesRecyclerView()
        observeCategoryMealsLiveData()
        onMealClick()
    }

    private fun onMealClick() {
        mealsAdapter.onItemClick={
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(Constants.MEAL_ID, it.idMeal)
            intent.putExtra(Constants.MEAL_NAME, it.strMeal)
            intent.putExtra(Constants.MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = mealsAdapter
        }
    }


    private fun getCategoryInformationFromIntent() {
        val intent = intent
        categoryName = intent.getStringExtra(CATEGORY_NAME)!!
        binding.tvCategoryDescription.text = intent.getStringExtra(CATEGORY_DESCRIPTION)!!
    }

    private fun observeCategoryMealsLiveData() {
        categoryMealsViewModel.getCategoryMealsLiveData().observe(
            this
        ) {
            mealsAdapter.setMeals(it as ArrayList<MealByCategory>)
            binding.tvCategoryMealsCount.text = "$categoryName\n Number of recipes: ${it.size}"
        }

    }
}