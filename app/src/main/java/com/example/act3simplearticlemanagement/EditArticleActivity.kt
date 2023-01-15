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
    private lateinit var btnSave: Button

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
        btnSave = findViewById(R.id.BArticleFormSubmit)

        btnSave.text = getString(R.string.update_article)

        btnSave.setOnClickListener {
            updateItem()
        }
    }

    private fun setupDatabase() {
        val articleCode = intent.getStringExtra(getString(R.string.article_code))!!

        db = Room.databaseBuilder(
            applicationContext,
            ArticleDatabase::class.java, getString(R.string.database_name)
        ).build()

        dao = db.articleDao()

        lifecycleScope.launch {
            article = dao.getArticle(articleCode)
        }.invokeOnCompletion { setupForm() }
    }

    private fun setupForm() {
        supportActionBar?.title = getString(R.string.edit_article, article.code)

        etCode.setText(article.code)
        etCode.isEnabled = false
        etStock.setText(article.stock.toString())
        etStock.isEnabled = false
        etPrice.setText(article.price.toString())
        etFamily.setText(article.family)
        etDescription.setText(article.description)
    }

    private fun updateItem() {
        var price = Float.NaN
        var valid = true

        if (etPrice.text.toString() != "") {
            price = try {
                etPrice.text.toString().toFloat()
            } catch (e: Exception) {
                Float.NaN
            }
        }

        if (!price.isFinite() || price < 0) {
            etPrice.error = getString(R.string.price_invalid)
            valid = false
        }

        if (etDescription.text.toString() == "") {
            etDescription.error = getString(R.string.description_empty)
            valid = false
        }

        if (valid) {
            lifecycleScope.launch {
                dao.update(
                    article.code,
                    article.stock,
                    etPrice.text.toString().toFloat(),
                    etFamily.text.toString(),
                    etDescription.text.toString()
                )
            }.invokeOnCompletion {
                db.close()
                finish()
            }
        }
    }
}
