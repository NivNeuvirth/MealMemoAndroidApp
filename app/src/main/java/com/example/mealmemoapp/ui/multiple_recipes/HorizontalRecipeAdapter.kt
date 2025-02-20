package com.example.mealmemoapp.ui.multiple_recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.RecipeLayoutBinding

class HorizontalRecipeAdapter(
    private val items: List<Recipe>,
    private val callback: ItemListener
) : RecyclerView.Adapter<HorizontalRecipeAdapter.ItemViewHolder>() {

    interface ItemListener {
        fun onItemClicked(recipe: Recipe)
        fun onFavoriteClicked(recipe: Recipe)
    }

    inner class ItemViewHolder(private val binding: RecipeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var recipe: Recipe

        init {
            binding.root.setOnClickListener(this)
            binding.favoriteButton.setOnClickListener {
                callback.onFavoriteClicked(recipe)
            }
        }

        override fun onClick(p0: View?) {
            callback.onItemClicked(recipe)
        }

        fun bind(item: Recipe) {
            this.recipe = item
            binding.itemTitle.text = item.title
            binding.itemTime.text = "${item.readyInMinutes} min"
            binding.itemServings.text = item.servings.toString()
            binding.itemScore.text = String.format("%.2f", item.spoonacularScore)

            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.itemImage)

            binding.favoriteButton.setImageResource(
                if (item.isFavorite) R.drawable.favorite_filled_24px else R.drawable.favorite_24px
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        RecipeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
