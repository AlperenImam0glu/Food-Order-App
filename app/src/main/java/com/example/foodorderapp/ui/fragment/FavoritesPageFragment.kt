package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.foodorderapp.databinding.FragmentFavoritesPageBinding
import com.example.foodorderapp.ui.viewmodel.FavoritePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesPageFragment : Fragment() {

    private lateinit var binding : FragmentFavoritesPageBinding
    private lateinit var viewModel: FavoritePageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesPageBinding.inflate(layoutInflater)
        val tempViewModel: FavoritePageViewModel by viewModels()
        viewModel = tempViewModel

        return binding.root
    }


}