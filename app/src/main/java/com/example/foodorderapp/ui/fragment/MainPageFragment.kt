package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodorderapp.R
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
    private lateinit var mainPageProductAdapter: MainPageProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainPageBinding.inflate(layoutInflater)
        val tempViewModel: MainPageViewModel by viewModels()
        viewModel = tempViewModel
        viewModel.getData()
        viewModel.getAllProduct()


        binding.rv.layoutManager =
            GridLayoutManager(requireContext(),2)

        binding.rv.addItemDecoration(
            MarginItemDecoration(16)
        )


        lifecycleScope.launchWhenStarted {
            viewModel.productFlow.collectLatest {
                viewModel.productFlow.value?.let {
                    mainPageProductAdapter = MainPageProductAdapter(it)
                    binding.rv.adapter = mainPageProductAdapter

                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.name.collectLatest {
                binding.textViewUserName.text = viewModel.name.value.toString()
            }
        }


        return binding.root
    }


}