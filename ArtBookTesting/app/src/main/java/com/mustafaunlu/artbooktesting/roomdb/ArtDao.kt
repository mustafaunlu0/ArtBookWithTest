package com.mustafaunlu.artbooktesting.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArtDao {


    //id ler çakışırsa OnConflict ile ne yapacağını söylüyoruz burada Replace ile yerine yaz dedik
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art : Art)

    @Delete
    suspend fun deleteArt(art : Art)

    @Query("SELECT * FROM arts")
    fun observeArts(): LiveData<List<Art>>

}