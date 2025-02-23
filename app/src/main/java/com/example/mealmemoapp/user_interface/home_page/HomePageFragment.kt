package com.example.mealmemoapp.user_interface.home_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mealmemoapp.R
import com.example.mealmemoapp.databinding.FragmentHomePageBinding
import com.example.mealmemoapp.utilities.autoCleared

class HomePageFragment : Fragment() {

    private var binding: FragmentHomePageBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recipesCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment2_to_homePageFragment)
        }

        binding.favoritesCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment2_to_favoriteRecipesFragment)
        }

        binding.nearByCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment2_to_storesNearbyFragment)
        }

        binding.ingredientsCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePageFragment2_to_getRecipeByIngredients)
        }
    }
}