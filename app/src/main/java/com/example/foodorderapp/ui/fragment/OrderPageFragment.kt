package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapp.data.model.cart.Cart
import com.example.foodorderapp.databinding.FragmentOrderPageBinding
import com.example.foodorderapp.ui.adapter.OrderPageProductAdapter
import com.example.foodorderapp.ui.viewmodel.OrderPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OrderPageFragment : Fragment() {

    private lateinit var binding: FragmentOrderPageBinding
    private lateinit var viewModel: OrderPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderPageBinding.inflate(layoutInflater)
        val tempViewModel: OrderPageViewModel by viewModels()
        viewModel = tempViewModel

        val mainPageProductAdapter = OrderPageProductAdapter(emptyList(), viewModel)
        binding.rv.adapter = mainPageProductAdapter

        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenCreated {
            viewModel.cartListFlow.collectLatest { products ->
                products?.let {
                    mainPageProductAdapter.productList=it.sepet_yemekler
                    val cartTotalPrice =viewModel.calculateCartTotalPrice(it.sepet_yemekler)
                    binding.textViewTotalPrice.text="$cartTotalPrice ₺"
                    mainPageProductAdapter.notifyDataSetChanged()
                }
            }
        }
        binding.rv.adapter = mainPageProductAdapter

        binding.buttonClearCart.setOnClickListener {
            viewModel.deleteAllProductInCart()
            mainPageProductAdapter.notifyDataSetChanged()
            mainPageProductAdapter.productList= ArrayList<Cart>()
            binding.rv.adapter = mainPageProductAdapter
            binding.textViewTotalPrice.text=""
        }

        return binding.root
    }



}