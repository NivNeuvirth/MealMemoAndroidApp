package com.example.mealmemoapp.ui.multiple_recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.databinding.ItemLayoutBinding


class RecipeAdapter(var items:List<Recipe>, val callback: ItemListener):RecyclerView.Adapter<RecipeAdapter.ItemViewHolder>() {

    private var filteredItems: List<Recipe> = items
    private var originalItems: List<Recipe> = items

    interface ItemListener{
        fun onItemClicked(index:Int)
        fun onFavoriteClicked(recipe: Recipe)
    }

    inner class ItemViewHolder(private val binding : ItemLayoutBinding)
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

        fun bind(item: Recipe) {
//            callback
            this.recipe = item
            binding.itemTitle.text = item.title
            binding.itemTime.text = "${item.readyInMinutes} min"
            binding.itemServings.text = item.servings.toString()
            binding.itemScore.text = String.format("%.2f", item.spoonacularScore)

            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.itemImage)

            // Set the favorite icon status based on isFavorite
            binding.favoriteButton.setImageResource(
                if (item.isFavorite) R.drawable.favorite_filled_24px else R.drawable.favorite_24px
            )
        }
    }

    fun itemAt(position:Int) = items[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=ItemViewHolder(
        ItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount()= items.size

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            originalItems
        } else {
            items.filter { it.title.contains(query, ignoreCase = true) }
        }
        items = filteredItems
        notifyDataSetChanged()
    }
}