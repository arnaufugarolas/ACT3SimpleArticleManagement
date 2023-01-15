package com.example.act3simplearticlemanagement

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setupMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupMenu(menu: Menu?) {
        supportActionBar?.title = "Add Article"
    }

    private fun init() {
        findViewById<Button>(R.id.BAddSubmit).setOnClickListener {
            insertItem()
        }
    }

    private fun insertItem() {
        val code = findViewById<EditText>(R.id.ETAddCode).text.toString()
        val description = findViewById<EditText>(R.id.ETAddDescription).text.toString()
        val family = findViewById<EditText>(R.id.ETAddFamily).text.toString()
        val price = findViewById<EditText>(R.id.ETAddPrice).text.toString().toFloat()
        val stock = findViewById<EditText>(R.id.ETAddStock).text.toString().toFloat()

        val db = Room.databaseBuilder(
            applicationContext,
            ArticleDatabase::class.java, "Articles"
        ).build()

        val dao = db.articleDao()
        var valid = false

        CoroutineScope(Dispatchers.IO).launch {
            valid = !dao.checkIfCodeExists(code)
        }.invokeOnCompletion {
            if (valid && code.isNotEmpty() && description.isNotEmpty() && price.isFinite() && stock.isFinite()) {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insert(code, description, family, price, stock)
                }.invokeOnCompletion { finish() }
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Article code already exists in the database or some fields are invalid",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }
    }
}