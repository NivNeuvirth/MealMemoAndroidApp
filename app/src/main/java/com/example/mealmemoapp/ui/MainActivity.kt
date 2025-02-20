package com.example.mealmemoapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mealmemoapp.R
import com.example.mealmemoapp.ui.multiple_recipes.HorizontalRecipeAdapter
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmemoapp.data.models.Category
import com.example.mealmemoapp.data.models.Recipe
import com.example.mealmemoapp.ui.multiple_recipes.VerticalRecipeAdapter

@AndroidEntryPoint
abstract class MainActivity : AppCompatActivity(),HorizontalRecipeAdapter.ItemListener {

   // private lateinit var mainRecyclerView:RecyclerView
   // private lateinit var verticalAdapter:VerticalRecipeAdapter
  //  private val categoryList= mutableListOf<Category>()




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
      //  setContentView(R.layout.fragment_home_page)
      //  mainRecyclerView=findViewById(R.id.mainPage)
      //  mainRecyclerView.layoutManager=LinearLayoutManager(this)

//        for (i in 1..4 ){
//            val recipes= mutableListOf<Recipe>()
//            for(j in 1..3){
//                recipes.add(
//                    Recipe(
//                        title = "Recipe $j",
//                        readyInMinutes = (10..60).random(),
//                        servings = (1..5).random(),
//                        spoonacularScore = (1..100).random().toFloat(),
//                        image = "https://via.placeholder.com/150",
//                        isFavorite = false,
//                        id = TODO(),
//                        summary = TODO(),
//                        extendedIngredients = TODO()
//                    )
//                )
//            }
//        }
        }
}