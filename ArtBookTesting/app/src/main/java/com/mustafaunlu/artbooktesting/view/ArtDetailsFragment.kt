package com.mustafaunlu.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.mustafaunlu.artbooktesting.R
import com.mustafaunlu.artbooktesting.databinding.FragmentArtDetailsBinding
import com.mustafaunlu.artbooktesting.databinding.FragmentArtsBinding
import com.mustafaunlu.artbooktesting.util.Status
import com.mustafaunlu.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide : RequestManager
) : Fragment(R.layout.fragment_art_details) {

    private var binding: FragmentArtDetailsBinding? =null
    lateinit var viewModel: ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        binding=FragmentArtDetailsBinding.bind(view)

        subscribeToObservers()



        binding!!.artDetailsImageView.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.toImageApiFragment())
        }


        //kullanıcı geri tuşuna bastığında ne olacak?
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding!!.saveButton.setOnClickListener {
            viewModel.makeArt(binding!!.nameText.text.toString(),
            binding!!.artistTextView.text.toString(),
                binding!!.yearTextView.text.toString()
                )
        }



    }

    private fun subscribeToObservers(){
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer {  url ->
            binding?.let {
                glide.load(url).into(it.artDetailsImageView)
            }

        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer{
            when(it.status){

                Status.SUCCESS -> {
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg()
                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(),"Error!",Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg()
                }

                Status.LOADING -> {

                }




            }




        })



    }


    override fun onDestroy() {
        binding=null
        super.onDestroy()
    }
}