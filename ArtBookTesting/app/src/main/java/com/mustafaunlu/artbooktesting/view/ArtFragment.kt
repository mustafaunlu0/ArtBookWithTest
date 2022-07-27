package com.mustafaunlu.artbooktesting.view

import android.content.ClipData
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mustafaunlu.artbooktesting.R
import com.mustafaunlu.artbooktesting.adapter.ArtRecyclerAdapter
import com.mustafaunlu.artbooktesting.databinding.FragmentArtsBinding
import com.mustafaunlu.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtFragment @Inject constructor(
    val artRecyclerAdapter : ArtRecyclerAdapter
): Fragment(R.layout.fragment_arts) {


    lateinit var viewModel : ArtViewModel
    private var fragmentBinding : FragmentArtsBinding ?=null


    //item sağa veya sola kaydırılınca ne olacağını yazıyoruz
    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedArt=artRecyclerAdapter.arts[layoutPosition]
            viewModel.deleteArt(selectedArt)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)
        fragmentBinding= FragmentArtsBinding.bind(view)

        subscribeToObservers()

        fragmentBinding!!.recyclerView.adapter=artRecyclerAdapter
        fragmentBinding!!.recyclerView.layoutManager=LinearLayoutManager(requireContext())
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(fragmentBinding!!.recyclerView)

        fragmentBinding!!.fab.setOnClickListener{
            findNavController().navigate(ArtFragmentDirections.toArtDetails())
        }


    }

private fun subscribeToObservers(){
  viewModel.artList.observe(viewLifecycleOwner, Observer {
       artRecyclerAdapter.arts=it
   })
}

override fun onDestroy() {
   super.onDestroy()
   fragmentBinding=null
}


}