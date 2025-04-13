package com.example.mealmemoapp.user_interface.multiple_recipes

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
import com.example.mealmemoapp.databinding.FragmentMultipleRecipesBinding
import com.example.mealmemoapp.utilities.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import com.example.mealmemoapp.utilities.Result

@AndroidEntryPoint
class MultipleRecipesFragment : Fragment(), RecipeAdapter.ItemListener {

    private var binding: FragmentMultipleRecipesBinding by autoCleared()
    private val viewModel: MultipleRecipesViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMultipleRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.recipes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val recipes = result.data
                    if (recipes != null) {
                        adapter.items = recipes
                        adapter.notifyDataSetChanged()
                    }
                }
                is Result.Failure -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.getRandomRecipes()

        binding.swipeRefreshLayout.setOnRefreshListener {

            viewModel.getRandomRecipes(forceRefresh = true)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter(emptyList(), this)
        binding.recyclerMainVertical.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMainVertical.adapter = adapter
    }

    override fun onItemClicked(index: Int) {
        val recipe = adapter.itemAt(index)
        val bundle = Bundle().apply {
            putParcelable("recipe", recipe)
        }
        findNavController().navigate(R.id.action_homePageFragment_to_detailedRecipeFragment, bundle)
    }

    override fun onFavoriteClicked(recipe: Recipe) {
        viewModel.updateFavoriteStatus(recipe)
        adapter.notifyDataSetChanged()
    }
}