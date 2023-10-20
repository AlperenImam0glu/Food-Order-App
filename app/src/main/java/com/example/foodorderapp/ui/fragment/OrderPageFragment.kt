package com.example.foodorderapp.ui.fragment

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
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
    var animationVisibility = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderPageBinding.inflate(layoutInflater)
        val tempViewModel: OrderPageViewModel by viewModels()
        viewModel = tempViewModel

        binding.emptyCartLayout.visibility = View.VISIBLE
        binding.buttonClearCart.visibility = View.INVISIBLE
        binding.cardViewOrder.visibility = View.INVISIBLE

        val mainPageProductAdapter = OrderPageProductAdapter(emptyList(), viewModel,requireContext())
        binding.rv.adapter = mainPageProductAdapter

        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenCreated {
            viewModel.cartListFlow.collectLatest { products ->
                products?.let {
                    if (it.sepet_yemekler.size != 0) {
                        binding.emptyCartLayout.visibility = View.GONE
                        binding.buttonClearCart.visibility = View.VISIBLE
                        binding.cardViewOrder.visibility = View.VISIBLE
                        binding.rv.visibility = View.VISIBLE
                    }

                    mainPageProductAdapter.productList = it.sepet_yemekler!!
                    val cartTotalPrice = viewModel.calculateCartTotalPrice(it.sepet_yemekler)
                    binding.textViewTotalPrice.text = "$cartTotalPrice ₺"
                    mainPageProductAdapter.notifyDataSetChanged()
                }

                if (products == null && !animationVisibility) {
                    binding.buttonClearCart.visibility = View.INVISIBLE
                    binding.cardViewOrder.visibility = View.INVISIBLE
                    binding.rv.visibility = View.INVISIBLE
                    binding.emptyCartLayout.visibility = View.VISIBLE
                }

                products?.let {
                    if (it.sepet_yemekler.size == 0) {
                        binding.emptyCartLayout.visibility = View.VISIBLE
                    }
                }

            }
        }
        binding.rv.adapter = mainPageProductAdapter

        binding.buttonClearCart.setOnClickListener {
            viewModel.deleteAllProductInCart()
            mainPageProductAdapter.notifyDataSetChanged()
            mainPageProductAdapter.productList = ArrayList<Cart>()
            binding.buttonClearCart.visibility = View.INVISIBLE
            binding.cardViewOrder.visibility = View.INVISIBLE
            binding.rv.adapter = mainPageProductAdapter
            binding.textViewTotalPrice.text = ""

            lottieAnimation()
        }

        return binding.root
    }

    fun lottieAnimation(){
        animationVisibility = true
        binding.lottieAnimation.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.lottieAnimation.visibility = View.VISIBLE
                binding.lottieAnimation.playAnimation()
            }, 0
        )

        binding.lottieAnimation.playAnimation()
        binding.lottieAnimation.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                binding.lottieAnimation.visibility = View.GONE
                animationVisibility = false
                binding.emptyCartLayout.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

        })

    }


}