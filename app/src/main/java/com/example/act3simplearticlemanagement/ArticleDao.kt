package com.example.act3simplearticlemanagement

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    suspend fun getAll(): MutableList<Article>

    @Query("INSERT INTO article VALUES (:code, :stock, :price, :family, :description)")
    suspend fun insert(
        code: String,
        stock: Float,
        price: Float,
        family: String?,
        description: String
    )

    @Query("UPDATE article SET stock = :stock, price = :price, family = :family, description = :description WHERE code = :code")
    suspend fun update(
        code: String,
        stock: Float,
        price: Float,
        family: String?,
        description: String
    )

    @Query("DELETE FROM article WHERE code = :code")
    suspend fun delete(code: String)

    @Query("SELECT * FROM article WHERE code = :code")
    suspend fun getArticle(code: String): Article

    @Query("SELECT EXISTS(SELECT 1 FROM article WHERE code = :code)")
    suspend fun checkIfCodeExists(code: String): Boolean

    @Query("SELECT * FROM article WHERE stock > 0.0 AND description LIKE '%' || :filterPhrase || '%'")
    suspend fun getAllPositiveStock(filterPhrase: String): MutableList<Article>

    @Query("SELECT * FROM article WHERE stock < 0.0 AND description LIKE '%' || :filterPhrase || '%'")
    suspend fun getAllNegativeStock(filterPhrase: String): MutableList<Article>

    @Query("SELECT * FROM article WHERE stock = 0.0 AND description LIKE '%' || :filterPhrase || '%'")
    suspend fun getAllZeroStock(filterPhrase: String): MutableList<Article>
}