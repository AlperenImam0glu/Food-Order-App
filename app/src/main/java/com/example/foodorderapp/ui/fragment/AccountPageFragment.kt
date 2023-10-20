package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.FragmentAccountPageBinding
import com.example.foodorderapp.databinding.FragmentProductDetailPageBinding
import com.example.foodorderapp.ui.viewmodel.AccountPageViewModel
import com.example.foodorderapp.ui.viewmodel.ProductDetailPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountPageFragment : Fragment() {

    private lateinit var binding: FragmentAccountPageBinding
    private lateinit var viewModel: AccountPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountPageBinding.inflate(layoutInflater)

        val tempViewModel: AccountPageViewModel by viewModels()
        viewModel = tempViewModel

        lifecycleScope.launchWhenCreated {
            viewModel.userInfoFlow.collectLatest {
                it?.let {
                    binding.textViewUserMail.text = it.email
                    binding.textViewUserName.text = it.name
                    binding.textViewUserLocation.text = it.location
                }
            }
        }

        return binding.root
    }


}