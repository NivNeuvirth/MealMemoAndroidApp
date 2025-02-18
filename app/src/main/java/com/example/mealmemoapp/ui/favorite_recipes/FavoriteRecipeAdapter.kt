package com.example.mealmemoapp.ui.favorite_recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.FragmentDetailedRecipeBinding
import com.example.mealmemoapp.databinding.FragmentHomePageBinding
import com.example.mealmemoapp.databinding.ItemLayoutBinding

class FavoriteRecipeAdapter(
    private val onRecipeClick: (Recipe) -> Unit,
    private val onFavoriteClick: (Recipe) -> Unit,
    private val onRemoveClick: (Recipe) -> Unit
) : ListAdapter<Recipe, FavoriteRecipeAdapter.FavoriteViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class FavoriteViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.itemTitle.text = recipe.title
            binding.itemTime.text = "${recipe.readyInMinutes} min"
            binding.itemServings.text = "${recipe.servings} servings"
            binding.itemScore.text = String.format("%.2f", recipe.spoonacularScore)

            Glide.with(binding.root.context)
                .load(recipe.image)
                .into(binding.itemImage)

            binding.root.setOnClickListener { onRecipeClick(recipe) }
            // Handle favorite button click
            binding.favoriteButton.apply {
                // Change button text/icon based on whether the recipe is marked as favorite
                if (recipe.isFavorite) {
                    setImageResource(R.drawable.favorite_filled_24px)  // Your filled favorite icon
                } else {
                    setImageResource(R.drawable.favorite_24px)  // Your outline favorite icon
                }

                setOnClickListener {
                    if (recipe.isFavorite) {
                        onRemoveClick(recipe)  // Remove from favorites
                    } else {
                        onFavoriteClick(recipe)  // Add to favorites
                    }
                }
            }
        }
    }

    class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
    }
}

