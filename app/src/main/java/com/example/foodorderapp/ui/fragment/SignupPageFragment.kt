package com.example.foodorderapp.ui.fragment

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.foodorderapp.data.model.Resource
import com.example.foodorderapp.databinding.FragmentRegisterPageBinding
import com.example.foodorderapp.ui.viewmodel.LoginPageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

        Log.e("deneme", "onCreateView çalıştı")
        collectFlow()

        binding.buttonLogin.setOnClickListener {
            signup()
        }
        binding.textViewLogin.setOnClickListener {
            findNavController(binding.buttonLogin).popBackStack()
        }


        return binding.root
    }

    fun signup() {
        val email = binding.editTextEmail.text.toString().trim()
        val name = binding.editTextName.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val location = binding.editTextLocation.text.toString().trim()
        viewModel.signup(name, email, password, location)
    }

    fun collectFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel?.signupFlow?.collectLatest {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "Başarısız ${it.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("hata", it.exception.toString())
                    }

                    Resource.Loading -> {
                        lottieOrder()
                    }

                    is Resource.Success -> {
                        findNavController(binding.buttonLogin).popBackStack()
                    }

                    else -> {}
                }
            }
        }
    }

    fun lottieOrder() {
        val lottie = binding.lottieLoginAnimation

        lottie.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                lottie.visibility = View.VISIBLE
                lottie.playAnimation()
            }, 0
        )

        lottie.playAnimation()
        lottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                lottie.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

        })
    }

}