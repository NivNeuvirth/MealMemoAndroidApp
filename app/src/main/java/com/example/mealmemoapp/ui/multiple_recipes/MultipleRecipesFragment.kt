package com.example.mealmemoapp.ui.multiple_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.FragmentHomePageBinding
import com.example.mealmemoapp.utils.Loading
import com.example.mealmemoapp.utils.Success
import com.example.mealmemoapp.utils.Error
import com.example.mealmemoapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultipleRecipesFragment : Fragment(), RecipeAdapter.ItemListener {

    private var binding: FragmentHomePageBinding by autoCleared()
    private val viewModel: MultipleRecipesViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Observe the LiveData for the recipes
        viewModel.recipes.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                is Success -> {
                    val recipes = resource.status.data
                    if (recipes != null) {
                        adapter.items = recipes
                        adapter.notifyDataSetChanged()
                    }
                }
                is Error -> {
                    Toast.makeText(requireContext(), resource.status.message, Toast.LENGTH_SHORT).show()
                }
                is Loading -> {
                    // Show loading state (optional)
                }
            }
        }

        // Trigger the initial data fetch when the fragment is created
        viewModel.getRandomRecipes()

        // Swipe-to-refresh listener
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Call the ViewModel function to fetch new data
            viewModel.getRandomRecipes(forceRefresh = true)

            // Stop the refresh animation when done
            binding.swipeRefreshLayout.isRefreshing = false
        }

        // Add the navigation listener for navigating to the favorites screen
        binding.favoritesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_favoriteRecipesFragment)
        }

        binding.storesNearbyBtn.setOnClickListener{
            findNavController().navigate(R.id.action_homePageFragment_to_storesNearbyFragment)
        }

        binding.generateRecipeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment_to_getRecipeByIngredients)
        }
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter(emptyList(), this)
        binding.recyclerMainVertical.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMainVertical.adapter = adapter
    }

    private fun observeMultipleRecipesData() {
        viewModel.recipes.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                is Success -> {
                    val recipes = resource.status.data
                    if (recipes != null) {
                        adapter.items = recipes
                        adapter.notifyDataSetChanged()
                    }
                }
                is Error -> {
                    Toast.makeText(requireContext(), resource.status.message, Toast.LENGTH_SHORT).show()
                }
                is Loading -> {
                    // Show loading indicator
                }
            }
        }
    }

    override fun onItemClicked(index: Int) {
        val recipe = adapter.itemAt(index)  // Get the clicked recipe
        val bundle = Bundle().apply {
            putParcelable("recipe", recipe)  // Put the Recipe object into the bundle
        }
        findNavController().navigate(R.id.action_homePageFragment_to_detailedRecipeFragment, bundle)
    }

    //     Handle the favorite click
    override fun onFavoriteClicked(recipe: Recipe) {
        viewModel.updateFavoriteStatus(recipe)
    }
}

