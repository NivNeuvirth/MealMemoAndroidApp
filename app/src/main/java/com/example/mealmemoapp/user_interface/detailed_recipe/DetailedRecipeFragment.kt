package com.example.mealmemoapp.user_interface.detailed_recipe

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.FragmentDetailedRecipeBinding
import com.example.mealmemoapp.utilities.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedRecipeFragment : Fragment() {

    private var binding: FragmentDetailedRecipeBinding by autoCleared()
    private val viewModel: DetailedRecipeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailedRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the Recipe object safely using BundleCompat
        val recipe = arguments?.let { BundleCompat.getParcelable(it, "recipe", Recipe::class.java) }

        if (recipe != null) {
            bindRecipeData(recipe)  // Update UI with recipe details
        } else {
            Toast.makeText(requireContext(), "Recipe not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindRecipeData(recipe: Recipe) {

        val formattedSummary: Spanned = Html.fromHtml(recipe.summary, Html.FROM_HTML_MODE_LEGACY)

        binding.itemTitle.text = recipe.title
        binding.itemDesc.text = formattedSummary
        binding.timeTitle.text = "Ready in ${recipe.readyInMinutes} minutes"
        binding.scoreTitle.text = String.format("%.2f Score", recipe.spoonacularScore)
        binding.servingsTitle.text = "${recipe.servings} Servings"

        // Display ingredients in the TextView
        val ingredients = recipe.extendedIngredients?.joinToString("\n") { "• ${it.name}" } ?: "No ingredients available"
        binding.itemIngredients.text = ingredients  // Add line breaks before each ingredient

        Glide.with(requireContext())
            .load(recipe.image)
            .into(binding.itemImage)

        binding.fabEdit.setOnClickListener{
            val recipeToEdit = viewModel.chosenItem.value

            Log.d("DetailedRecipeFragment", "Recipe to Edit: $recipeToEdit") // Debugging

            val bundle = Bundle().apply {
                putParcelable("recipe_to_add_or_edit", recipeToEdit)
            }

            findNavController().navigate(R.id.action_detailedRecipeFragment_to_addOrEditRecipeFragment, bundle)
        }
    }
}