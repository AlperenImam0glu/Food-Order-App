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
import com.example.foodorderapp.data.model.product.Yemekler
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
        Log.d("MyFragment", "onCreateView")

        binding.rv.layoutManager =StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        //GridLayoutManager(requireContext(), 2)

        binding.rv.addItemDecoration(
            MarginItemDecoration(16)
        )
        val mainPageProductAdapter = MainPageProductAdapter(emptyList(), viewModel)
        binding.rv.adapter = mainPageProductAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.productFlow.collectLatest { products ->
                products?.let {
                    mainPageProductAdapter.productList=it
                    mainPageProductAdapter.notifyDataSetChanged()
                }
            }
        }
        binding.rv.adapter = mainPageProductAdapter


        lifecycleScope.launchWhenStarted {
            viewModel.userInfoFlow.collectLatest {
                binding.textViewUserName.text = viewModel.userInfoFlow.value?.name.toString()
                binding.textViewToolbar.text = viewModel.userInfoFlow.value?.location.toString()
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProduct()
    }



}