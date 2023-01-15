package com.example.act3simplearticlemanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch

class EditArticleActivity : AppCompatActivity() {
    private lateinit var db: ArticleDatabase
    private lateinit var dao: ArticleDao
    private lateinit var article: Article
    private lateinit var etCode: EditText
    private lateinit var etStock: EditText
    private lateinit var etPrice: EditText
    private lateinit var etFamily: EditText
    private lateinit var etDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_form)

        init()
    }

    private fun init() {
        setupVariables()
        setupDatabase()
    }

    private fun setupVariables() {
        etCode = findViewById(R.id.ETArticleFormCode)
        etStock = findViewById(R.id.ETArticleFormStock)
        etPrice = findViewById(R.id.ETArticleFormPrice)
        etFamily = findViewById(R.id.ETArticleFormFamily)
        etDescription = findViewById(R.id.ETArticleFormDescription)
    }

    private fun setupDatabase() {
        val articleCode = intent.getStringExtra("articleCode")!!

        db = Room.databaseBuilder(
            applicationContext,
            ArticleDatabase::class.java, "Articles"
        ).build()

        dao = db.articleDao()

        lifecycleScope.launch {
            article = dao.getArticle(articleCode)
        }.invokeOnCompletion { setupForm() }
    }

    private fun setupForm() {
        supportActionBar?.title = "Edit Article: ${article.code}"

        etCode.setText(article.code)
        etCode.isEnabled = false
        etStock.setText(article.stock.toString())
        etStock.isEnabled = false
        etPrice.setText(article.price.toString())
        etFamily.setText(article.family)
        etDescription.setText(article.description)

        findViewById<Button>(R.id.BArticleFormSubmit).setOnClickListener {
            if (etPrice.text.isNotEmpty() && etDescription.text.isNotEmpty()) {
                lifecycleScope.launch {
                    dao.update(
                        article.code,
                        etDescription.text.toString(),
                        etFamily.text.toString(),
                        etPrice.text.toString().toFloat(),
                        article.stock,
                    )
                }.invokeOnCompletion {
                    db.close()
                    finish()
                }
            } else {
                if (etPrice.text.isEmpty()) etPrice.error = "Price is required"
                if (etDescription.text.isEmpty()) etDescription.error = "Description is required"
            }
        }
    }
}