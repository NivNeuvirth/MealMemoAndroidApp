package com.example.mealmemoapp.user_interface.favorite_recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
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
                val bundle = Bundle().apply {
                    putParcelable("recipe", recipe)
                }
                findNavController().navigate(R.id.action_favoriteRecipesFragment_to_detailedRecipeFragment, bundle)
            },
            onFavoriteClick = { recipe ->
                favoriteRecipesViewModel.addFavorite(recipe)
            },
            onRemoveClick = { recipe ->
                favoriteRecipesViewModel.removeFavorite(recipe)
            }
        )

        favoriteRecipesViewModel.favoriteRecipes.observe(viewLifecycleOwner, Observer { recipes ->
            favoriteRecipesAdapter.setFullList(recipes)
        })

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                favoriteRecipesAdapter.filterList(newText.orEmpty())
                return true
            }
        })

        binding.recyclerViewFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteRecipesAdapter
        }

        favoriteRecipesViewModel.favoriteRecipes.observe(viewLifecycleOwner, Observer { recipes ->
            favoriteRecipesAdapter.submitList(recipes)
        })

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteRecipesFragment_to_addOrEditRecipeFragment)
        }
    }
}

