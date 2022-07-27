package com.mustafaunlu.artbooktesting.adapter

import android.text.Layout
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.mustafaunlu.artbooktesting.R
import com.mustafaunlu.artbooktesting.roomdb.Art
import org.w3c.dom.Text
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
    val glide : RequestManager
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>(){


    class ArtViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    //Recycler i view u daha verimli(hızlı) hale getiren bir yapı. RecycleView üzerinde bir güncelleme yapılacağı zaman kullanılır
    //Alttaki iki fonksiyonda karşılaştırma fonksiyonlarıdır.
    private val diffUtil = object : DiffUtil.ItemCallback<Art>(){
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem==newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var arts : List<Art>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.art_row,parent,false)
        return ArtViewHolder(view)

    }

    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val imageView=holder.itemView.findViewById<ImageView>(R.id.artRowImageView)
        val nameText=holder.itemView.findViewById<TextView>(R.id.artRowNameTextView)
        val artistNameText=holder.itemView.findViewById<TextView>(R.id.artRowArtistTextView)
        val yearText=holder.itemView.findViewById<TextView>(R.id.artRowYearTextView)

        val art=arts[position]
        holder.itemView.apply {
            nameText.text="Name: ${art.name}"
            artistNameText.text="Artist Name ${art.artistName}"
            yearText.text="Year: ${art.year}"
            glide.load(art.flag).into(imageView)
        }

    }

    override fun getItemCount(): Int {
        return arts.size
    }
}