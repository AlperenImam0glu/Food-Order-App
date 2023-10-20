package com.example.foodorderapp.ui.fragment

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.foodorderapp.data.model.Resource
import com.example.foodorderapp.databinding.FragmentLoginPageBinding
import com.example.foodorderapp.ui.viewmodel.LoginPageViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class LoginPageFragment : Fragment() {

    private lateinit var binding: FragmentLoginPageBinding
    lateinit var viewModel: LoginPageViewModel
    private lateinit var navBar:BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginPageBinding.inflate(layoutInflater)
        val tempViewModel: LoginPageViewModel by viewModels()
        viewModel = tempViewModel

         getActivity()?.findViewById<BottomNavigationView>(com.example.foodorderapp.R.id.bottomNav)?.let {
             navBar = it
         }
        if (navBar != null) {
            navBar.visibility = View.GONE
        }
        collectFlow()

        binding.buttonLogin.setOnClickListener {
            try{

                login()
            }catch (e:Exception){
                Log.e("loginviewmodel","${e.message}")
            }
        }

        binding.textViewSingup.setOnClickListener {
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
                try{
                    when (it) {
                        is Resource.Failure -> {
                            Toast.makeText(requireContext(), "Başarısız", Toast.LENGTH_SHORT).show()
                        }
                        Resource.Loading -> {
                            Toast.makeText(requireContext(), "Bekleniyor", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Success -> {
                            val action = LoginPageFragmentDirections.navigateToMainPageFragment()
                            Navigation.findNavController(binding.texrViewTitle).navigate(action)
                            navBar.visibility = View.VISIBLE
                        }
                        else -> {}
                    }
                }catch (e:Exception){
                    Log.e("loginviewmodel","${e.message}")
                }
            }


        }
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.currentUser !=null){
            val action = LoginPageFragmentDirections.navigateToMainPageFragment()
            Navigation.findNavController(binding.buttonLogin).navigate(action)
            navBar.visibility= View.VISIBLE
        }else{
           viewModel.resetFlows()
        }
    }

    override fun onPause() {
        super.onPause()
        val tempViewModel: LoginPageViewModel by viewModels()
        viewModel = tempViewModel
    }


}