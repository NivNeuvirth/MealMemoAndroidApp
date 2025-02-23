package com.example.mealmemoapp.user_interface.multiple_recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.RecipeLayoutBinding


class RecipeAdapter(var items:List<Recipe>, val callback: ItemListener):RecyclerView.Adapter<RecipeAdapter.ItemViewHolder>() {

    interface ItemListener{
        fun onItemClicked(index:Int)
        fun onFavoriteClicked(recipe: Recipe)
    }

    inner class ItemViewHolder(private val binding : RecipeLayoutBinding)
        :RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        private lateinit var recipe : Recipe

        init {
            binding.root.setOnClickListener(this)
            binding.favoriteButton.setOnClickListener{
                callback.onFavoriteClicked(recipe)
            }
        }

        override fun onClick(p0: View?) {
            callback.onItemClicked(adapterPosition)
        }

        fun bind(recipe: Recipe) {

            this.recipe = recipe
            binding.itemTitle.text = recipe.title
            binding.itemTime.text = binding.root.context.getString(R.string.time_format, recipe.readyInMinutes)
            binding.itemServings.text = binding.root.context.getString(R.string.servings_format, recipe.servings)
            binding.itemScore.text = binding.root.context.getString(R.string.score_format, recipe.spoonacularScore.toInt())

            Glide.with(binding.root.context)
                .load(recipe.image)
                .into(binding.itemImage)

            binding.favoriteButton.setImageResource(
                if (recipe.isFavorite) R.drawable.favorite_filled_24px else R.drawable.favorite_24px
            )
        }
    }

    fun itemAt(position:Int) = items[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=ItemViewHolder(
        RecipeLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount()= items.size
}