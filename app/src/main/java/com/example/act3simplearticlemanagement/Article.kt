package com.example.act3simplearticlemanagement

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Article(
    @PrimaryKey val code: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "family") val family: String?,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "stock") val stock: Float
)
