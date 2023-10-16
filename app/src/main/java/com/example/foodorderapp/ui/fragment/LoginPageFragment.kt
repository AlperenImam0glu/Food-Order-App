package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.foodorderapp.data.model.Resource
import com.example.foodorderapp.databinding.FragmentLoginPageBinding
import com.example.foodorderapp.ui.viewmodel.LoginPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginPageFragment : Fragment() {

    private lateinit var binding: FragmentLoginPageBinding
    lateinit var viewModel: LoginPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginPageBinding.inflate(layoutInflater)
        val tempViewModel: LoginPageViewModel by viewModels()
        viewModel = tempViewModel

        collectFlow()

        binding.buttonLogin.setOnClickListener {
            login()
        }

        binding.textViewRegiter.setOnClickListener {
            val action = LoginPageFragmentDirections.navigateToRegisterPage()
            Navigation.findNavController(it).navigate(action)
        }

        return binding.root
    }

    fun login() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        viewModel.login(email, password)
    }

    fun collectFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel?.loginFlow?.collectLatest {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(requireContext(), "Başarısız", Toast.LENGTH_SHORT).show()
                    }

                    Resource.Loading -> {
                        Toast.makeText(requireContext(), "Bekleniyor", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Başarılı", Toast.LENGTH_SHORT).show()
                        val action = LoginPageFragmentDirections.navigateToMainPageFragment()
                        Navigation.findNavController(binding.buttonLogin).navigate(action)
                    }

                    else -> {}
                }
            }


        }
    }
}