package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodorderapp.R
import com.example.foodorderapp.data.entitiy.Resource
import com.example.foodorderapp.databinding.FragmentLoginPageBinding
import com.example.foodorderapp.databinding.FragmentRegisterPageBinding
import com.example.foodorderapp.ui.viewmodel.LoginPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignupPageFragment : Fragment() {

    lateinit var viewModel: LoginPageViewModel
    private lateinit var binding: FragmentRegisterPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterPageBinding.inflate(layoutInflater)
        val tempViewModel: LoginPageViewModel by viewModels()
        viewModel = tempViewModel

        collectFlow()

        binding.buttonLogin.setOnClickListener {
            signup()
        }


        return binding.root
    }

    fun signup() {
        val email = binding.editTextEmail.text.toString().trim()
        val name = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        viewModel.signup(name,email, password)
    }

    fun collectFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel?.signupFlow?.collectLatest {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(requireContext(), "Başarısız ${it.exception}", Toast.LENGTH_SHORT).show()
                        Log.e("hata",it.exception.toString())
                    }

                    Resource.Loading -> {
                        Toast.makeText(requireContext(), "Bekleniyor", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Başarılı", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }


        }
    }

}