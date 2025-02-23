package com.example.mealmemoapp.user_interface.add_or_edit_recipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Ingredient
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.FragmentAddOrEditRecipeBinding
import com.example.mealmemoapp.user_interface.favorite_recipes.FavoriteRecipesViewModel
import com.example.mealmemoapp.utilities.autoCleared
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class AddOrEditRecipeFragment : Fragment() {

    private var binding: FragmentAddOrEditRecipeBinding by autoCleared()
    private var imageURI: Uri? = null
    private val viewModel: FavoriteRecipesViewModel by activityViewModels()

    val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.uploadedImage.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            imageURI = it
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddOrEditRecipeBinding.inflate(inflater, container, false)

        val recipe = arguments?.getParcelable<Recipe>("recipe_to_edit")

        Log.d("AddOrEditRecipeFragment", "Received Recipe: $recipe") // Debugging
        Log.d("AddOrEditRecipeFragment", "Title: ${recipe?.title}")
        Log.d("AddOrEditRecipeFragment", "Image URI: ${recipe?.image}")



        if (recipe != null) {
            binding.recipeTitle.setText(recipe.title)
            binding.timeInput.setText(recipe.readyInMinutes.toString())
            binding.servingsText.setText(recipe.servings.toString())
            binding.scoreInput.setText(recipe.spoonacularScore.toString())
            binding.recipeDesc.setText(recipe.summary)
            // binding.recipeIngre.text = recipe.extendedIngredients  // Uncomment if needed
            imageURI = Uri.parse(recipe.image)
        }
        binding.uploadedImage.setImageURI(imageURI)

        binding.finishBtn.setOnClickListener {

            val title = binding.recipeTitle.text.toString().trim()
            val time = binding.timeInput.text.toString().trim().toIntOrNull() ?: 0
            val servings = binding.servingsText.text.toString().trim().toIntOrNull() ?: 0
            val score = binding.scoreInput.text.toString().trim().toFloat()
            val description = binding.recipeDesc.text.toString().trim()
            val ingredients = binding.recipeIngre.text.toString().trim()

            val ingredientsList = ingredients
                .split("\n")
                .filter { it.isNotBlank() }
                .mapIndexed { index, ingredient -> Ingredient(index, ingredient) }

            val formattedIngredients = ingredientsList.joinToString("\n")

            val updatedRecipe = Recipe(
                recipe?.id ?: 0,
                imageURI?.toString() ?: "",
                title,
                time,
                servings,
                description,
                ingredientsList,
                score,
                isFavorite = recipe?.isFavorite ?: false
                )


            if (recipe == null) {
                //updatedRecipe.id = it.id
                viewModel.addFavorite(updatedRecipe)
            } else {
                viewModel.updateRecipe(updatedRecipe)
            }

            findNavController().navigate(R.id.action_addOrEditRecipeFragment_to_favoriteRecipesFragment)
        }

        binding.imagePickBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        return binding.root
    }
}