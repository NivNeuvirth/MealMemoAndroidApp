package com.example.mealmemoapp.ui.add_or_edit_recipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Ingredient
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.FragmentAddOrEditRecipeBinding
import com.example.mealmemoapp.ui.favorite_recipes.FavoriteRecipesViewModel
import com.example.mealmemoapp.ui.multiple_recipes.MultipleRecipesViewModel
import com.example.mealmemoapp.utils.autoCleared
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

        val recipe = arguments?.getParcelable("recipe_to_add_or_edit") as? Recipe

        Log.d("AddOrEditRecipeFragment", "Received Recipe: $recipe") // Debugging

        recipe?.let {
            binding.recipeTitle.setText(it.title)
            binding.timeInput.setText(it.readyInMinutes)
            binding.servingsText.text = it.servings.toString()
            binding.scoreInput.setText(it.spoonacularScore.toString())
            binding.recipeDesc.setText(it.summary)
            //binding.recipeIngre.text = it.extendedIngredients
            imageURI = Uri.parse(it.image)
            binding.uploadedImage.setImageURI(imageURI)
        }

        binding.timeInput.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(0) // Default hour
                .setMinute(0) // Default minute
                .setTitleText("Select Time")
                .build()

            picker.addOnPositiveButtonClickListener {
                val hour = picker.hour
                val minute = picker.minute
                val selectedTime = String.format("%02d:%02d", hour, minute)
                binding.timeInput.setText(selectedTime)
            }

            picker.show(parentFragmentManager, picker.toString())
        }

        binding.finishBtn.setOnClickListener {
            // Validate inputs
            val title = binding.recipeTitle.text.toString().trim()
            val time = binding.timeInput.text.toString().trim().toIntOrNull() ?: 0
            val servings = binding.servingsText.text.toString().trim().toIntOrNull() ?: 0
            val score = binding.scoreInput.text.toString().trim().toFloat()
            val description = binding.recipeDesc.text.toString().trim()
            val ingredients = binding.recipeIngre.text.toString().trim()

            // Check for empty or null inputs
//            when {
//                title.isEmpty() -> {
//                    Toast.makeText(requireContext(),
//                        "Title can not be empty", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//                time.isEmpty() -> {
//                    Toast.makeText(requireContext(),
//                        "time_cannot_be_empty", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//                difficulty == getString(R.string.not_selected) -> {
//                    Toast.makeText(requireContext(),
//                        getString(R.string.please_select_a_difficulty_level), Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//                calories.isEmpty() -> {
//                    Toast.makeText(requireContext(),
//                        getString(R.string.calories_cannot_be_empty), Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//                description.isEmpty() -> {
//                    Toast.makeText(requireContext(),
//                        getString(R.string.description_cannot_be_empty), Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//                ingredients.isEmpty() -> {
//                    Toast.makeText(requireContext(),
//                        getString(R.string.ingredients_cannot_be_empty), Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//            }

            // Process the ingredients
            val ingredientsList = ingredients
                .split("\n")
                .filter { it.isNotBlank() }
                .mapIndexed { index, ingredient -> Ingredient(index, ingredient) }

            val formattedIngredients = ingredientsList.joinToString("\n")

            // Create the updated item
            val updatedRecipe = Recipe(
                recipe?.id ?: 0,
                imageURI?.toString() ?: "",
                title,
                time,
                servings,
                description,
                ingredientsList,
                score,
                isFavorite = true
                )

            // Check if updating or adding a new item
            if (recipe == null) {
                updatedRecipe.id = it.id
                viewModel.updateRecipe(updatedRecipe)
                //viewModel.addFavorite(updatedRecipe) // Add new favorite
            } else {
                viewModel.updateFavoriteStatus(updatedRecipe) // Update existing favorite
            }

            // Navigate back to the list
            findNavController().navigate(R.id.action_addOrEditRecipeFragment_to_favoriteRecipesFragment)
        }

        binding.imagePickBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        return binding.root
    }
}