package com.example.act3simplearticlemanagement

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.airbnb.paris.Paris
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleAdapter(private val context: Context, private val mList: MutableList<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.article, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(holder, mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun bind(holder: ViewHolder, itemsViewModel: Article) {
        holder.delete.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setMessage(context.getString(R.string.delete_confirmation))
                .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                    deleteItem(
                        itemsViewModel,
                        it
                    )
                }
                .setNeutralButton(context.getString(R.string.cancel)) { _, _ -> }
                .show()
        }

        holder.itemView.setOnClickListener {
            val i = Intent(context, EditArticleActivity::class.java)
            i.putExtra(context.getString(R.string.article_code), itemsViewModel.code)
            it.context.startActivity(i)
        }

        when (itemsViewModel.family) {
            context.getString(R.string.software) -> Paris.style(holder.item).apply(R.style.ItemArticleSoftware)
            context.getString(R.string.hardware) -> Paris.style(holder.item).apply(R.style.ItemArticleHardware)
            context.getString(R.string.other) -> Paris.style(holder.item).apply(R.style.ItemArticleOther)
            else -> Paris.style(holder.item).apply(R.style.ItemArticleNone)
        }

        if (itemsViewModel.stock <= 0) {
            holder.stock.setTextAppearance(R.style.ItemArticleDataRed)
        }

        holder.code.text = itemsViewModel.code
        holder.stock.text = itemsViewModel.stock.toString()
        holder.basePrice.text = context.getString(
            R.string.price,
            itemsViewModel.price.toString()
        ).replace(context.getString(R.string.dot), context.getString(R.string.coma))
        holder.taxedPrice.text = context.getString(
            R.string.price,
            (itemsViewModel.price * 1.21).toString()
        ).replace(context.getString(R.string.dot), context.getString(R.string.coma))
        holder.description.text = itemsViewModel.description
    }

    private fun deleteItem(itemsViewModel: Article, view: View) {
        val position = mList.indexOf(itemsViewModel)

        val db = Room.databaseBuilder(
            context,
            ArticleDatabase::class.java, context.getString(R.string.database_name)
        ).build()

        CoroutineScope(Dispatchers.IO).launch {
            db.articleDao().delete(itemsViewModel.code)
            db.close()
        }

        mList.removeAt(position)
        notifyItemRemoved(position)
        Snackbar.make(view, context.getString(R.string.article_deleted), Snackbar.LENGTH_LONG)
            .show()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val item: ConstraintLayout = itemView.findViewById(R.id.CLArticle)
        val code: TextView = itemView.findViewById(R.id.TVArticleCode)
        val description: TextView = itemView.findViewById(R.id.TVArticleDescription)
        val basePrice: TextView = itemView.findViewById(R.id.TVArticlePriceBase)
        val taxedPrice: TextView = itemView.findViewById(R.id.TVArticlePriceTaxed)
        val stock: TextView = itemView.findViewById(R.id.TVArticleStock)
        val delete: ImageView = itemView.findViewById(R.id.IVArticleDelete)
    }
}