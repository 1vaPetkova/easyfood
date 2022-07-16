package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.R
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.adapters.CategoriesAdapter
import com.example.easyfood.adapters.CategoriesTabAdapter
import com.example.easyfood.databinding.FragmentCategoriesBinding
import com.example.easyfood.entities.Category
import com.example.easyfood.utils.Constants
import com.example.easyfood.viewModel.HomeViewModel


class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesTabAdapter
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).homeViewModel
        categoriesAdapter = CategoriesTabAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCategoriesRecyclerView()
        homeViewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(Constants.CATEGORY_NAME, it.strCategory)
            intent.putExtra(Constants.CATEGORY_DESCRIPTION, it.strCategoryDescription)
            startActivity(intent)
        }
    }

    private fun observeCategoriesLiveData() {
        homeViewModel.getCategoriesLiveData().observe(viewLifecycleOwner) {
            categoriesAdapter.setCategories(it as ArrayList<Category>)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.rvCats.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }
}