package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodorderapp.databinding.FragmentMainPageBinding
import com.example.foodorderapp.ui.adapter.MainPageProductAdapter
import com.example.foodorderapp.ui.viewmodel.MainPageViewModel
import com.example.foodorderapp.utils.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainPageFragment : Fragment() {

    private lateinit var binding: FragmentMainPageBinding
    private lateinit var viewModel: MainPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainPageBinding.inflate(layoutInflater)
        val tempViewModel: MainPageViewModel by viewModels()
        viewModel = tempViewModel
        viewModel.getData()

        binding.rv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        binding.rv.addItemDecoration(
            MarginItemDecoration(16)
        )
        val mainPageProductAdapter =
            MainPageProductAdapter(emptyList(), viewModel, requireContext(), emptyList())
        binding.rv.adapter = mainPageProductAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.combineFlow.collectLatest {
                mergeData(mainPageProductAdapter)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.productListFlow.collectLatest { products ->
                products?.let {
                    mainPageProductAdapter.productList = it
                    viewModel.favoritesDataFlow.value?.let {
                        mainPageProductAdapter.favoriteList = it
                    }
                    mainPageProductAdapter.notifyDataSetChanged()
                }
            }
        }
        binding.rv.adapter = mainPageProductAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.userInfoFlow.collectLatest {
                it?.let {
                    binding.textViewUserNameSubtitle.visibility = View.VISIBLE
                    binding.textViewUserName.text = viewModel.userInfoFlow.value?.name.toString()
                    binding.textViewToolbar.text = viewModel.userInfoFlow.value?.location.toString()
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                if (newText != null) { searchProduct(newText,mainPageProductAdapter) }
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) { searchProduct(query,mainPageProductAdapter) }
                return false
            }
        })
        return binding.root
    }

    fun mergeData(mainPageProductAdapter: MainPageProductAdapter) {
        val productList = viewModel.mergeFlowsData()
        mainPageProductAdapter.productList = productList
        viewModel.favoritesDataFlow.value?.let {
            mainPageProductAdapter.favoriteList = it
        }
        mainPageProductAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProduct()
        viewModel.getProductsInCart()
        viewModel.getProductInDB()
    }

    fun searchProduct(searchString: String,mainPageProductAdapter: MainPageProductAdapter) {
        val k = viewModel.searchProduct(searchString)
        mainPageProductAdapter.productList = k!!
        mainPageProductAdapter.notifyDataSetChanged()
        Log.e("Search","Çalıltı")
        viewModel.getProductInDB()
    }


}