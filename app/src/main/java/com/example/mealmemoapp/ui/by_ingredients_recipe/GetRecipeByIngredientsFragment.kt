package com.example.mealmemoapp.ui.by_ingredients_recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.FragmentGetRecipeByIngredientsBinding
import com.example.mealmemoapp.ui.multiple_recipes.RecipeAdapter
import com.example.mealmemoapp.utils.Loading
import com.example.mealmemoapp.utils.Success
import com.example.mealmemoapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetRecipeByIngredientsFragment : Fragment(), RecipeAdapter.ItemListener {

    private var binding: FragmentGetRecipeByIngredientsBinding by autoCleared()
    private val viewModel: GetRecipeByIngredientsViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetRecipeByIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.searchButton.setOnClickListener {
            val ingredients = binding.ingredientsEditText.text.toString()
            if (ingredients.isNotEmpty()) {
                viewModel.searchRecipesByIngredients(ingredients)
            }
        }

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
                    Toast.makeText(requireContext(), resource.status.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is Loading -> {
                    // Show loading state if needed
                }

                is com.example.mealmemoapp.utils.Error -> TODO()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter(emptyList(), this)
        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recipesRecyclerView.adapter = adapter
    }

    override fun onItemClicked(index: Int) {
        val recipe = adapter.itemAt(index)
        val bundle = Bundle().apply {
            putParcelable("recipe", recipe)
        }
        findNavController().navigate(R.id.action_getRecipeByIngredients_to_detailedRecipeFragment, bundle)
    }

    override fun onFavoriteClicked(recipe: Recipe) {
        viewModel.updateFavoriteStatus(recipe)
    }
}