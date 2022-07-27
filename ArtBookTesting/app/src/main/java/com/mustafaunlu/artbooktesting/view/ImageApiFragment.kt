package com.mustafaunlu.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mustafaunlu.artbooktesting.R
import com.mustafaunlu.artbooktesting.adapter.ImageRecyclerAdapter
import com.mustafaunlu.artbooktesting.databinding.FragmentImageApiBinding
import com.mustafaunlu.artbooktesting.util.Status
import com.mustafaunlu.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageApiFragment @Inject constructor(
     val imageRecyclerAdapter: ImageRecyclerAdapter

) : Fragment(R.layout.fragment_image_api) {

    private var binding : FragmentImageApiBinding? = null
    lateinit var viewModel : ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)
        binding= FragmentImageApiBinding.bind(view)



        var job : Job? = null

        binding!!.searchText.addTextChangedListener {
            job?.cancel()
            job=lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.searchForImage(it.toString())


                    }
                }
            }
        }

        subscribeToObservers()

        binding!!.imageRecyclerView.adapter=imageRecyclerAdapter
        binding!!.imageRecyclerView.layoutManager=GridLayoutManager(requireContext(),3)
        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }




    }
    private fun subscribeToObservers(){

        viewModel.Imagelist.observe(viewLifecycleOwner, Observer {
            when(it.status){

                Status.SUCCESS -> {

                    val urls= it.data?.hits?.map {  imageResult ->
                    imageResult.previewURL
                    }

                    imageRecyclerAdapter.images=urls ?: listOf()

                    binding?.progressBar?.visibility=View.GONE

                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                    binding?.progressBar?.visibility=View.GONE



                }

                Status.LOADING -> {
                    binding?.progressBar?.visibility=View.VISIBLE



                }
            }
        })
    }
}