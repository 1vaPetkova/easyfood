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
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.adapters.MostPopularMealsAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.entities.Category
import com.example.easyfood.entities.Meal
import com.example.easyfood.entities.MealByCategory
import com.example.easyfood.fragments.bottomSheet.MealBottomSheetFragment
import com.example.easyfood.utils.Constants.Companion.CATEGORY_DESCRIPTION
import com.example.easyfood.utils.Constants.Companion.CATEGORY_NAME
import com.example.easyfood.utils.Constants.Companion.MEAL_ID
import com.example.easyfood.utils.Constants.Companion.MEAL_NAME
import com.example.easyfood.utils.Constants.Companion.MEAL_THUMB
import com.example.easyfood.utils.Constants.Companion.YOUTUBE_URI
import com.example.easyfood.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularMealsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).homeViewModel
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
        onPopularItemLongClick()
        //Categories Recycler view
        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()
    }


    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, it.strCategory)
            intent.putExtra(CATEGORY_DESCRIPTION, it.strCategoryDescription)
            startActivity(intent)
        }
    }

    private fun observeCategoriesLiveData() {
        homeViewModel.getCategoriesLiveData().observe(viewLifecycleOwner) {
            categoriesAdapter.setCategories(it as ArrayList<Category>)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = {
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Popular meal info")
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
            popularItemsAdapter.setMeals(it as ArrayList<MealByCategory>)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.rvPopularItems.apply {
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
