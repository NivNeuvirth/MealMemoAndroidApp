package com.example.mealmemoapp.ui.multiple_recipes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmemoapp.data.models.Category
import com.example.mealmemoapp.databinding.VerticleRecipeLayoutBinding

class VerticalRecipeAdapter(
    private val context: Context,
    private val categories: List<Category>,
    private val callback: HorizontalRecipeAdapter.ItemListener
) : RecyclerView.Adapter<VerticalRecipeAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: VerticleRecipeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.categoryTitle.text = category.categoryName

            // Setup Horizontal RecyclerView
            binding.recyclerVertical.layoutManager=
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerVertical.adapter = HorizontalRecipeAdapter(category.recipes, callback)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        VerticleRecipeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
}