package com.example.mealmemoapp.user_interface.favorite_recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.RecipeLayoutBinding

class FavoriteRecipeAdapter(
    private val onRecipeClick: (Recipe) -> Unit,
    private val onFavoriteClick: (Recipe) -> Unit,
    private val onRemoveClick: (Recipe) -> Unit
) : ListAdapter<Recipe, FavoriteRecipeAdapter.FavoriteViewHolder>(RecipeDiffCallback()) {

    private var fullRecipeList: List<Recipe> = listOf()

    fun setFullList(recipes: List<Recipe>) {
        fullRecipeList = recipes
        submitList(fullRecipeList)
    }

    fun filterList(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullRecipeList
        } else {
            fullRecipeList.filter { it.title.contains(query, ignoreCase = true) }
        }
        submitList(filteredList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = RecipeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    inner class FavoriteViewHolder(private val binding: RecipeLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.itemTitle.text = recipe.title
            binding.itemTime.text = binding.root.context.getString(R.string.time_format, recipe.readyInMinutes)
            binding.itemServings.text = binding.root.context.getString(R.string.servings_format, recipe.servings)
            binding.itemScore.text = binding.root.context.getString(R.string.score_format, recipe.spoonacularScore.toInt())

            Glide.with(binding.root.context)
                .load(recipe.image)
                .into(binding.itemImage)

            binding.root.setOnClickListener { onRecipeClick(recipe) }

            binding.favoriteButton.apply {

                if (recipe.isFavorite) {
                    setImageResource(R.drawable.favorite_filled_24px)
                } else {
                    setImageResource(R.drawable.favorite_24px)
                }

                setOnClickListener {
                    if (recipe.isFavorite) {
                        onRemoveClick(recipe)
                    } else {
                        onFavoriteClick(recipe)
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