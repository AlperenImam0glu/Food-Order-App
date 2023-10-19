package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.FragmentFavoritesPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesPageFragment : Fragment() {

    private lateinit var binding : FragmentFavoritesPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesPageBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }


}