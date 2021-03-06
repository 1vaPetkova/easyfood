package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.entities.Meal
import com.example.easyfood.utils.Constants.Companion.MEAL_ID
import com.example.easyfood.utils.Constants.Companion.MEAL_NAME
import com.example.easyfood.utils.Constants.Companion.MEAL_THUMB
import com.example.easyfood.utils.Constants.Companion.YOUTUBE_URI
import com.example.easyfood.viewModel.MealViewModel
import com.example.easyfood.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private var youtubeUrl: String? = null
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealViewModel: MealViewModel
    private var mealToSave: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //create mealViewModel
        val viewModelFactory = MealViewModelFactory(MealDatabase.getInstance(this))
        mealViewModel = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        loadingCase()
        setInformationInViews()
        mealViewModel.getMealDetails(mealId)
        observeMealDetailsLiveData()
        onYoutubeImageClick()
        onFavouriteClick()
    }

    private fun onFavouriteClick() {
        binding.btnAddToFavourites.setOnClickListener {
            mealToSave?.let {
                mealViewModel.insertMealToFavourites(it)
                Toast.makeText(this, "Meal saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imageYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            startActivity(intent)
        }
    }

    private fun observeMealDetailsLiveData() {
        mealViewModel.getMealDetailsData().observe(
            this
        ) {
            onResponseCase()
            mealToSave = it
            youtubeUrl = it.strYoutube
            binding.tvCategory.text = "Category: ${it!!.strCategory}"
            binding.tvArea.text = "Area: ${it.strArea}"
            binding.tvInstructionsSteps.text = it.strInstructions
        }
    }

    private fun setInformationInViews() {
        Glide
            .with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(MEAL_ID)!!
        mealName = intent.getStringExtra(MEAL_NAME)!!
        mealThumb = intent.getStringExtra(MEAL_THUMB)!!
        youtubeUrl = intent.getStringExtra(YOUTUBE_URI)
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFavourites.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imageYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFavourites.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imageYoutube.visibility = View.VISIBLE
    }
}