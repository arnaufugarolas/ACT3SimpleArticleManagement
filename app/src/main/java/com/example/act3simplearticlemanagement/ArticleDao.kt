package com.example.act3simplearticlemanagement

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    suspend fun getAll(): MutableList<Article>

    @Query("SELECT * FROM article WHERE stock <= 0")
    suspend fun getOutOfStock(): List<Article>

    @Query("INSERT INTO article VALUES (:code, :description, :family, :price, :stock)")
    suspend fun insert(code: String, description: String, family: String?, price: Float, stock: Float)

    @Query("UPDATE article SET description = :description, family = :family, price = :price, stock = :stock WHERE code = :code")
    suspend fun update(code: String, description: String, family: String?, price: Float, stock: Float)

    @Query("DELETE FROM article WHERE code = :code")
    suspend fun delete(code: String)

    @Query("DELETE FROM article")
    suspend fun deleteAll()

    @Query("SELECT * FROM article WHERE code = :code")
    suspend fun getArticle(code: String): Article

    @Query("SELECT * FROM article WHERE family = :family")
    suspend fun getArticlesByFamily(family: String): List<Article>

    @Query("SELECT EXISTS(SELECT 1 FROM article WHERE code = :code)")
    suspend fun checkIfCodeExists(code: String): Boolean
}