package com.example.act3simplearticlemanagement

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

interface FilterDescriptionInterface {
    fun filter(phrase: String)
}

class MainActivity : AppCompatActivity(), FilterDescriptionInterface {
    private lateinit var db: ArticleDatabase
    private lateinit var dao: ArticleDao
    private lateinit var articles: MutableList<Article>
    private lateinit var recyclerViewArticles: RecyclerView
    private lateinit var stockMutableList: MutableList<MenuItem>
    private var filterPhrase: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onResume() {
        checkDB()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupMenu(menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun filter(phrase: String) {
        filterPhrase = phrase
        changeFilter()
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
                dao.insert("ART-023435", 1.0f, 10.0f, "SOFTWARE", "Article 1")
                dao.insert("ART-034354", 2.0f, 5.0f, "HARDWARE", "Article 2")
                dao.insert("ART-043524", 0.0f, 3.0f, "OTHER", "Article 3")
                dao.insert("ART-054346", -4.0f, 0.0f, "NONE", "Article 4")
            }
            articles = dao.getAll()
        }.invokeOnCompletion { setupRecyclerView() }
    }

    private fun changeFilter() {
        val checked = stockMutableList.filter { it.isChecked }
        val filteredArticles = mutableListOf<Article>()

        lifecycleScope.launch {
            if (checked.contains(stockMutableList[0])) {
                filteredArticles.addAll(dao.getAllPositiveStock(filterPhrase))
            }
            if (checked.contains(stockMutableList[1])) {
                filteredArticles.addAll(dao.getAllNegativeStock(filterPhrase))
            }
            if (checked.contains(stockMutableList[2])) {
                filteredArticles.addAll(dao.getAllZeroStock(filterPhrase))
            }
        }.invokeOnCompletion {
            recyclerViewArticles.adapter = ArticleAdapter(applicationContext, filteredArticles)
        }

    }

    private fun setupRecyclerView() {
        recyclerViewArticles.adapter = ArticleAdapter(applicationContext, articles)
        recyclerViewArticles.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun checkDB() {
        if (this::articles.isInitialized) {
            changeFilter()
        }
    }

    private fun setupMenu(menu: Menu) {
        supportActionBar?.title = getString(R.string.articles)
        menuInflater.inflate(R.menu.menu, menu)

        val miAdd = menu.findItem(R.id.MIAdd)!!
        val miAllStock = menu.findItem(R.id.MIAllStock)!!
        val miPositiveStock = menu.findItem(R.id.MIPositiveStock)!!
        val miNegativeStock = menu.findItem(R.id.MINegativeStock)!!
        val miZeroStock = menu.findItem(R.id.MIZeroStock)!!
        val miDescription = menu.findItem(R.id.MIDescription)!!

        stockMutableList = mutableListOf(
            miPositiveStock,
            miNegativeStock,
            miZeroStock
        )

        miAdd.setOnMenuItemClickListener {
            val intent = Intent(applicationContext, AddArticleActivity::class.java)
            startActivity(intent)
            true
        }

        miAllStock.setOnMenuItemClickListener {
            if (miAllStock.isChecked) {
                miAllStock.isChecked = false
                stockMutableList.forEach { it.isChecked = false }
            } else {
                miAllStock.isChecked = true
                stockMutableList.forEach { it.isChecked = true }
            }
            changeFilter()
            true
        }

        for (menuItem in stockMutableList) {
            menuItem.setOnMenuItemClickListener {
                menuItem.isChecked = !menuItem.isChecked
                miAllStock.isChecked =
                    miPositiveStock.isChecked && miNegativeStock.isChecked && miZeroStock.isChecked
                changeFilter()
                true
            }
        }

        miDescription.setOnMenuItemClickListener {
            val dialog = DescriptionDialogFragment()
            dialog.show(supportFragmentManager, "DescriptionDialogFragment")
            true
        }
    }
}
