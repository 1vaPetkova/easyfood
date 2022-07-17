package com.example.easyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapters.FavouriteMealsAdapter
import com.example.easyfood.databinding.FragmentFavouritesBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.entities.Meal
import com.example.easyfood.utils.Constants
import com.example.easyfood.viewModel.HomeViewModel
import com.example.easyfood.viewModel.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavouritesFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels(factoryProducer = {
        HomeViewModelFactory(MealDatabase.getInstance(requireContext()))
    })
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favouritesAdapter: FavouriteMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouritesAdapter = FavouriteMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareFavouriteMealsRecyclerView()
        homeViewModel.getFavouriteMealsLiveData()
        observeFavouritesLiveData()
        onMealClick()

        val itemTouchHelper =
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = true

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val mealToDelete = favouritesAdapter.differ.currentList[position]
                    homeViewModel.deleteMeal(mealToDelete)
                    Snackbar.make(
                        requireView(),
                        "Meal ${mealToDelete.strMeal} deleted!",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Undo") {
                            homeViewModel.insertMealToFavourites(mealToDelete)
                        }
                        .show()
                }
            }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavourites)
    }


    private fun onMealClick() {
        favouritesAdapter.onItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(Constants.MEAL_ID, it.idMeal)
            intent.putExtra(Constants.MEAL_NAME, it.strMeal)
            intent.putExtra(Constants.MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareFavouriteMealsRecyclerView() {
        binding.rvFavourites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favouritesAdapter
        }
    }


    private fun observeFavouritesLiveData() {
        homeViewModel.getFavouriteMealsLiveData().observe(viewLifecycleOwner) {
            favouritesAdapter.differ.submitList(it as ArrayList<Meal>)
        }
    }
}