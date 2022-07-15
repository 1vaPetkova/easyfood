package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.entities.MealByCategory
import com.example.easyfood.entities.MealsByCategoryList
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryMealsViewModel() : ViewModel() {
    private var categoryMealsLiveData = MutableLiveData<List<MealByCategory>>()
    fun getCategoryMeals(categoryName: String) {
        RetrofitInstance.api.getCategoryMeals(categoryName)
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    response.body()?.let {
                        categoryMealsLiveData.postValue(it.meals)
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("CategoryMealsViewModel", t.message.toString())
                }
            })
    }

    fun getCategoryMealsLiveData(): LiveData<List<MealByCategory>> {
        return categoryMealsLiveData
    }

}