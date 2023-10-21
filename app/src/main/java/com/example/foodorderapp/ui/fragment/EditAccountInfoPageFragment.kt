package com.example.foodorderapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodorderapp.databinding.FragmentEditAccountInfoPageBinding
import com.example.foodorderapp.ui.viewmodel.EditAccountInfoPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAccountInfoPageFragment : Fragment() {


    lateinit var binding: FragmentEditAccountInfoPageBinding
    lateinit var viewModel: EditAccountInfoPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAccountInfoPageBinding.inflate(layoutInflater)
        val tempViewModel: EditAccountInfoPageViewModel by viewModels()
        viewModel = tempViewModel

        val bundle: EditAccountInfoPageFragmentArgs by navArgs()
        val location = bundle.location

        binding.editText.setText(location)
        binding.buttonLogOut.setOnClickListener {
            try{
                val newLocation = binding.editText.text.toString()
                viewModel.changeLocationInFirabase(newLocation.trim())
                Toast.makeText(requireContext(),"Güncelleme Başarılı",Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }catch (e:Exception){
                Toast.makeText(requireContext(),"Güncelleme Başarısız",Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

}