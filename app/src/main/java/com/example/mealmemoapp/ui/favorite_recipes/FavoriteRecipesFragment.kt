package com.example.mealmemoapp.ui.favorite_recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmemoapp.R
import com.example.mealmemoapp.databinding.FragmentFavoriteRecipesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {

    private lateinit var binding: FragmentFavoriteRecipesBinding
    private lateinit var favoriteRecipesAdapter: FavoriteRecipeAdapter
    private lateinit var favoriteRecipesViewModel: FavoriteRecipesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoriteRecipesBinding.bind(view)
        favoriteRecipesViewModel = ViewModelProvider(this).get(FavoriteRecipesViewModel::class.java)

        favoriteRecipesAdapter = FavoriteRecipeAdapter(
            onRecipeClick = { recipe ->
                // Handle recipe click, navigate to detailed recipe
                findNavController().navigate(R.id.action_favoriteRecipesFragment_to_detailedRecipeFragment)
            },
            onFavoriteClick = { recipe ->
                favoriteRecipesViewModel.addFavorite(recipe)  // This will add to favorites
            },
            onRemoveClick = { recipe ->
                favoriteRecipesViewModel.removeFavorite(recipe)  // This will remove from favorites
            }
        )

        binding.recyclerViewFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteRecipesAdapter
        }

        favoriteRecipesViewModel.favoriteRecipes.observe(viewLifecycleOwner, Observer { recipes ->
            favoriteRecipesAdapter.submitList(recipes)
        })
    }
}

