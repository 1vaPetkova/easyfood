package com.example.easyfood.fragments.bottomSheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.databinding.FragmentMealBottomSheetBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.utils.Constants
import com.example.easyfood.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThumb: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = (activity as MainActivity).homeViewModel
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let {
            homeViewModel.getMealById(it)
        }
        observeBottomSheetMeal()
        onReadMoreClick()
    }

    private fun onReadMoreClick() {
        binding.tvReadMore.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            if (mealName != null && mealThumb != null) {
                intent.putExtra(Constants.MEAL_ID, mealId)
                intent.putExtra(Constants.MEAL_NAME, mealName)
                intent.putExtra(Constants.MEAL_THUMB, mealThumb)
            }
            startActivity(intent)
        }
    }

    private fun observeBottomSheetMeal() {
        homeViewModel.getPopularMealLiveData().observe(viewLifecycleOwner) {
            Glide
                .with(this)
                .load(it.strMealThumb)
                .into(binding.imgBottomSheet)
            binding.tvAreaBottomSheet.text = it.strArea
            binding.tvCategoryBottomSheet.text = it.strCategory
            binding.tvMealName.text = it.strMeal
            mealName = it.strMeal
            mealThumb = it.strMealThumb
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }

    }
}