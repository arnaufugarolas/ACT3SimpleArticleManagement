package com.example.act3simplearticlemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var db: ArticleDatabase
    private lateinit var dao: ArticleDao
    private lateinit var articles: List<Article>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        setupDatabase()
    }

    private fun setupDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            ArticleDatabase::class.java, "Articles"
        ).build()

        dao = db.articleDao()

        lifecycleScope.launch {
            articles = dao.getAll()
        }
    }

}
