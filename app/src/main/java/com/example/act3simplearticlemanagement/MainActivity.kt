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
            // dao.insert("CODE", "DESCRIPTION", "SOFTWARE, HARDWARE, OTHER or null", 1.0f, 1.0f)
            // insert data with random values with the format of the previous line with random code and description

            dao.insert("A-1", "Description 1", "SOFTWARE", 1.0f, 1.0f)
            dao.insert("A-2", "Description 2", "SOFTWARE", 2.0f, 2.0f)
            dao.insert("A-3", "Description 3", "SOFTWARE", 3.0f, 3.0f)
            dao.insert("A-4", "Description 4", "SOFTWARE", 4.0f, 4.0f)
            dao.insert("A-5", "Description 5", "SOFTWARE", 5.0f, 5.0f)
            dao.insert("A-6", "Description 6", "SOFTWARE", 6.0f, 6.0f)
            dao.insert("B-1", "Description 1", "HARDWARE", 1.0f, 1.0f)
            dao.insert("B-2", "Description 2", "HARDWARE", 2.0f, 2.0f)
            dao.insert("B-3", "Description 3", "HARDWARE", 3.0f, 3.0f)
            dao.insert("B-4", "Description 4", "HARDWARE", 4.0f, 4.0f)
            dao.insert("B-5", "Description 5", "HARDWARE", 5.0f, 5.0f)
            dao.insert("C-1", "Description 1", null, 1.0f, 1.0f)
            dao.insert("C-2", "Description 2", null, 2.0f, 2.0f)
            dao.insert("D-1", "Description 1", "OTHER", 1.0f, 1.0f)
            dao.insert("D-2", "Description 2", "OTHER", 2.0f, 2.0f)
            dao.insert("D-3", "Description 3", "OTHER", 3.0f, 3.0f)
            dao.insert("D-4", "Description 4", "OTHER", 4.0f, 4.0f)
            dao.insert("D-5", "Description 5", "OTHER", 5.0f, 5.0f)
            dao.insert("E-1", "", "SOFTWARE", 1.0f, 1.0f)
            dao.insert("E-2", "", "SOFTWARE", 2.0f, 2.0f)
            dao.insert("E-3", "", "SOFTWARE", 3.0f, 3.0f)
            dao.insert("E-4", "", "SOFTWARE", 4.0f, 4.0f)
            dao.insert("E-5", "", "SOFTWARE", 5.0f, 5.0f)

            Log.d("Articles", articles.toString())
        }

    }
}