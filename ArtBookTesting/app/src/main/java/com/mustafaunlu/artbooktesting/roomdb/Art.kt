package com.mustafaunlu.artbooktesting.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arts")
data class Art(
    var name : String,
    var artistName : String,
    var year : Int,
    var flag : String,
    @PrimaryKey(autoGenerate = true)
    var userId : Int? = null

)