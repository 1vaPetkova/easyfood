package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.adapters.MostPopularMealsAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.entities.Category
import com.example.easyfood.entities.Meal
import com.example.easyfood.entities.MealByCategory
import com.example.easyfood.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularMealsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.fragments.thumbMeal"
        const val YOUTUBE_URI = "com.example.easyfood.fragments.youtubeUri"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.getRandomMeal()
        popularItemsAdapter = MostPopularMealsAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.tvHome.setOnClickListener { homeViewModel.getRandomMeal() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Random meal
        observeRandomMeal()
        onRandomMealClick()
        //Popular items Recycler view
        preparePopularItemsRecyclerView()
        homeViewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()
        //Categories Recycler view
        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        observeCategoriesLiveData()
    }


    private fun observeCategoriesLiveData() {
        homeViewModel.getCategoriesLiveData().observe(viewLifecycleOwner) {
            categoriesAdapter.setCategories(categories = it as ArrayList<Category>)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recyclerCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, it.idMeal)
            intent.putExtra(MEAL_NAME, it.strMeal)
            intent.putExtra(MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observePopularItemsLiveData() {
        homeViewModel.getPopularItemsLiveData().observe(viewLifecycleOwner) {
            popularItemsAdapter.setMeals(mealsList = it as ArrayList<MealByCategory>)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recyclerPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            intent.putExtra(YOUTUBE_URI, randomMeal.strYoutube)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        homeViewModel.getRandomMealLiveData()
            .observe(viewLifecycleOwner) {
                Glide
                    .with(this@HomeFragment)
                    .load(it!!.strMealThumb)
                    .placeholder(R.drawable.ic_food)
                    .into(binding.imgRandomMeal)
                this.randomMeal = it
                binding.randomMealName.text = it.strMeal
            }
    }


}
