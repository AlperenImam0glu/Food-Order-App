package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodorderapp.data.model.databasemodel.DataBaseProductModel
import com.example.foodorderapp.databinding.FragmentFavoritesPageBinding
import com.example.foodorderapp.ui.adapter.FavoritePageProductAdapter
import com.example.foodorderapp.ui.adapter.MainPageProductAdapter
import com.example.foodorderapp.ui.viewmodel.FavoritePageViewModel
import com.example.foodorderapp.utils.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesPageFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesPageBinding
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
        binding.emptyListLayout.visibility = View.VISIBLE

        val mainPageProductAdapter = FavoritePageProductAdapter(emptyList(), viewModel, requireContext())
        binding.rv.adapter = mainPageProductAdapter

        val favoritesList = ArrayList<DataBaseProductModel>()
        viewModel.cartListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                favoritesList.clear()
                for (i in it) {
                    if (i.kullanici_id == viewModel.currentUser?.uid) {
                        favoritesList.add(i) }
                }
                mainPageProductAdapter.productList = favoritesList
                binding.rv.adapter = mainPageProductAdapter
                if (it.size > 0) {
                    binding.emptyListLayout.visibility = View.GONE
                }else{
                    binding.emptyListLayout.visibility = View.VISIBLE
                }
                Log.e("gelen veri türü", "liste ${favoritesList.size}")
                Log.e("gelen veri türü", "it ${it.size}")
            }

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProductInDB()
    }


}