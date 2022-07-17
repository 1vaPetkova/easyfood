package com.example.easyfood.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.easyfood.R
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.viewModel.HomeViewModel
import com.example.easyfood.viewModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val homeViewModel: HomeViewModel by viewModels(factoryProducer = {
        HomeViewModelFactory(MealDatabase.getInstance(this))
    })
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        addSwipeToRefresh(swipeRefreshLayout)
    }

    private fun addSwipeToRefresh(swipeRefreshLayout: SwipeRefreshLayout?) {
        swipeRefreshLayout?.setOnRefreshListener {
            homeViewModel.getRandomMeal(true)
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
