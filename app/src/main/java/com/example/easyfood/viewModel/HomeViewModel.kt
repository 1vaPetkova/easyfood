package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.entities.*
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favouriteMealsLiveData = mealDatabase.mealDao().getAllMeals()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                response.body()?.let {
                    randomMealLiveData.postValue(it.meals[0])
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Vegetarian")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    response.body()?.let {
                        popularItemsLiveData.postValue(it.meals)
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("HomeViewModel", t.message.toString())
                }
            })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {
                    categoriesLiveData.postValue(it.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun insertMealToFavourites(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun getRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun getPopularItemsLiveData(): LiveData<List<MealByCategory>> {
        return popularItemsLiveData
    }

    fun getCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun getFavouriteMealsLiveData(): LiveData<List<Meal>> {
        return favouriteMealsLiveData
    }
}
