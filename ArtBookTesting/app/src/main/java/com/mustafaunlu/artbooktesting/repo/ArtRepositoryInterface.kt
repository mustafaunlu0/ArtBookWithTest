package com.mustafaunlu.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.mustafaunlu.artbooktesting.model.ImageResponse
import com.mustafaunlu.artbooktesting.roomdb.Art
import com.mustafaunlu.artbooktesting.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art : Art)

    suspend fun deleteArt(art : Art)

    fun getArt() : LiveData<List<Art>>

    suspend fun searchImage(imageString : String) : Resource<ImageResponse>
}