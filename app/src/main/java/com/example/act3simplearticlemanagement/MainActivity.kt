package com.example.act3simplearticlemanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var db: ArticleDatabase
    private lateinit var dao: ArticleDao
    private lateinit var articles: MutableList<Article>
    private lateinit var recyclerViewArticles: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onStart() {
        checkDB()
        super.onStart()
    }

    private fun checkDB() {
        if (this::articles.isInitialized) {
            var currentArticles = mutableListOf<Article>()

            lifecycleScope.launch {
                currentArticles = dao.getAll()
            }.invokeOnCompletion {
                if (currentArticles != articles) {
                    articles = currentArticles
                    recyclerViewArticles.adapter = ArticleAdapter(applicationContext, articles)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupMenu(menu: Menu?) {
        menuInflater.inflate(R.menu.menu, menu)
        supportActionBar?.title = getString(R.string.articles)
        menu?.findItem(R.id.MIAdd)?.setOnMenuItemClickListener {
            val intent = Intent(this, AddArticleActivity::class.java)
            startActivity(intent)
            true
        }
    }

    private fun init() {
        setupVariables()
        setupDatabase()
    }

    private fun setupVariables() {
        recyclerViewArticles = findViewById(R.id.RVArticles)
    }

    private fun setupDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            ArticleDatabase::class.java, getString(R.string.database_name)
        ).build()

        dao = db.articleDao()

        lifecycleScope.launch {
            if (dao.getAll().isEmpty()) {
                dao.insert("ART-023435", 1.0f, 1.0f, "SOFTWARE", "Article 1")
                dao.insert("ART-034354", 2.0f, 2.0f, "HARDWARE", "Article 2")
                dao.insert("ART-043524", 3.0f, 3.0f, "OTHER", "Article 3")
                dao.insert("ART-054346", 4.0f, 4.0f, "NONE", "Article 4")
            }
            articles = dao.getAll()
        }.invokeOnCompletion { setupRecyclerView() }
    }

    private fun setupRecyclerView() {
        recyclerViewArticles.adapter = ArticleAdapter(applicationContext, articles)
        recyclerViewArticles.layoutManager = LinearLayoutManager(applicationContext)
    }
}
