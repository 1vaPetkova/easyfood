package com.example.easyfood.retrofit

import com.example.easyfood.entities.CategoryList
import com.example.easyfood.entities.MealsByCategoryList
import com.example.easyfood.entities.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName: String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>

    @GET("filter.php")
    fun getCategoryMeals(@Query("c") categoryName: String): Call<MealsByCategoryList>
}
