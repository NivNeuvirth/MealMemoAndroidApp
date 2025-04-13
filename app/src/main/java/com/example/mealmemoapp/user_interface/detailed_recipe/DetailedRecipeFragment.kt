package com.example.mealmemoapp.user_interface.detailed_recipe

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailedRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeToEdit = arguments?.getParcelable("recipe") as? Recipe
        val isFromFavorites = arguments?.getBoolean("isFromFavorites", false) ?: false

        if (recipeToEdit != null) {
            bindRecipeData(recipeToEdit)
        } else {
            Toast.makeText(requireContext(),
                getString(R.string.no_recipe_found_txt), Toast.LENGTH_SHORT).show()
        }

        if (isFromFavorites) {
            binding.fabEdit.visibility = View.VISIBLE
        } else {
            binding.fabEdit.visibility = View.GONE
        }

        binding.fabEdit.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("recipe_to_edit", recipeToEdit)
            }
            findNavController().navigate(R.id.action_detailedRecipeFragment_to_addOrEditRecipeFragment, bundle)
        }
    }

    private fun bindRecipeData(recipe: Recipe) {

        val formattedSummary: Spanned = Html.fromHtml(recipe.summary, Html.FROM_HTML_MODE_LEGACY)

        binding.itemTitle.text = recipe.title
        binding.itemDesc.text = formattedSummary
        binding.timeTitle.text = binding.root.context.getString(R.string.ready_in_minutes, recipe.readyInMinutes)
        binding.scoreTitle.text = getString(R.string.score_format, recipe.spoonacularScore.toInt())
        binding.servingsTitle.text = getString(R.string.servings_format, recipe.servings)

        val ingredients = recipe.extendedIngredients?.joinToString("\n") { "• ${it.name}" } ?: "No ingredients available"
        binding.itemIngredients.text = ingredients

        Glide.with(requireContext())
            .load(recipe.image)
            .into(binding.itemImage)
    }
}