package com.example.mealmemoapp.ui.multiple_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmemoapp.databinding.FragmentHomePageBinding
import com.example.mealmemoapp.utils.Loading
import com.example.mealmemoapp.utils.Success
import com.example.mealmemoapp.utils.Error
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultipleRecipesFragment : Fragment(), RecipeAdapter.ItemListener {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MultipleRecipesViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeRecipeData(1) // Fetching recipe with ID 1 (Modify as needed)
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter(emptyList(), this)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
    }

    private fun observeRecipeData(recipeId: Int) {
        viewModel.getRecipe(recipeId).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {  // Accessing the status inside Resource
                is Success -> {
                    val recipe = resource.status.data // Extract data from Success
                    if (recipe != null) {
                        adapter.items = listOf(recipe)
                        adapter.notifyDataSetChanged()
                    }
                }
                is Error -> {
                    Toast.makeText(requireContext(), resource.status.message, Toast.LENGTH_SHORT).show()
                }
                is Loading -> {
                    // Do nothing (No progress bar)
                }
            }
        }
    }

    override fun onItemClicked(index: Int) {
        // Handle recipe item click here if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}