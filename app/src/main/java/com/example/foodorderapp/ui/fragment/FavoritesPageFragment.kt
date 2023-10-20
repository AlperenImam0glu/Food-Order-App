package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodorderapp.databinding.FragmentFavoritesPageBinding
import com.example.foodorderapp.ui.adapter.FavoritePageProductAdapter
import com.example.foodorderapp.ui.adapter.MainPageProductAdapter
import com.example.foodorderapp.ui.viewmodel.FavoritePageViewModel
import com.example.foodorderapp.utils.MarginItemDecoration
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

        binding.rv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.rv.addItemDecoration(
            MarginItemDecoration(16)
        )

        val mainPageProductAdapter = FavoritePageProductAdapter(emptyList(), viewModel)
        binding.rv.adapter = mainPageProductAdapter

        viewModel.cartListLiveData.observe(viewLifecycleOwner){
            it?.let {
                mainPageProductAdapter.productList = it
                mainPageProductAdapter.notifyDataSetChanged()
                binding.rv.adapter = mainPageProductAdapter
            }

        }



        return binding.root
    }


}