package com.example.mealmemoapp.ui.detailed_recipe

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.FragmentDetailedRecipeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedRecipeFragment : Fragment() {

    private var _binding: FragmentDetailedRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedRecipeBinding.inflate(inflater, container, false)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}