package com.example.act3simplearticlemanagement

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    suspend fun getAll(): MutableList<Article>

    @Query("INSERT INTO article VALUES (:code, :stock, :price, :family, :description)")
    suspend fun insert(code: String, stock: Float, price: Float, family: String?, description: String)

    @Query("UPDATE article SET stock = :stock, price = :price, family = :family, description = :description WHERE code = :code")
    suspend fun update(code: String, stock: Float, price: Float, family: String?, description: String)

    @Query("DELETE FROM article WHERE code = :code")
    suspend fun delete(code: String)

    @Query("DELETE FROM article")
    suspend fun deleteAll()

    @Query("SELECT * FROM article WHERE code = :code")
    suspend fun getArticle(code: String): Article

    @Query("SELECT EXISTS(SELECT 1 FROM article WHERE code = :code)")
    suspend fun checkIfCodeExists(code: String): Boolean
}