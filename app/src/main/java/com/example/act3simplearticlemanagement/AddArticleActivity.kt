package com.example.act3simplearticlemanagement

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddArticleActivity : AppCompatActivity() {
    private lateinit var db: ArticleDatabase
    private lateinit var dao: ArticleDao
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.title = "Add Article"

        return super.onCreateOptionsMenu(menu)
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

        btnSave.setOnClickListener {
            insertItem()
        }
    }

    private fun setupDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            ArticleDatabase::class.java, "Articles"
        ).build()

        dao = db.articleDao()
    }


    private fun insertItem() {
        val code = etCode.text.toString()
        var stock = Float.NaN
        var price = Float.NaN
        val family = etFamily.text.toString()
        val description = etDescription.text.toString()
        var codeExists = false


        if (etStock.text.toString() != "") {
            stock = try {
                etStock.text.toString().toFloat()
            } catch (e: Exception) {
                Float.NaN
            }
        }

        if (etPrice.text.toString() != "") {
            price = try {
                etPrice.text.toString().toFloat()
            } catch (e: Exception) {
                Float.NaN
            }
        }

        lifecycleScope.launch {
            codeExists = dao.checkIfCodeExists(code)
        }.invokeOnCompletion {
            var valid = true
            runOnUiThread {
                if (code.isEmpty()) {
                    etCode.error = "Code cannot be empty"
                    valid = false
                } else if (codeExists) {
                    etCode.error = "Code already exists"
                    valid = false
                }
                if (!stock.isFinite() || stock <= 0) {
                    etStock.error = "Stock must be a positive number"
                    valid = false
                }
                if (!price.isFinite() || price <= 0) {
                    etPrice.error = "Price must be a positive number"
                    valid = false
                }
                if (description.isEmpty()) {
                    etDescription.error = "Description cannot be empty"
                    valid = false
                }
            }

            if (valid) {
                lifecycleScope.launch {
                    dao.insert(code, description, family, price, stock)
                }.invokeOnCompletion { finish() }
            } else {
                Snackbar.make(
                    findViewById(R.id.SVArticleForm),
                    "Invalid data",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}
