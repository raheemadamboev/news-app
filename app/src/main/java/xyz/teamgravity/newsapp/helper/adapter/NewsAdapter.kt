package xyz.teamgravity.newsapp.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import xyz.teamgravity.newsapp.databinding.CardArticleBinding
import xyz.teamgravity.newsapp.model.ArticleModel

class NewsAdapter(private val listener: OnNewsListener) : ListAdapter<ArticleModel, NewsAdapter.NewsViewHolder>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel) =
                oldItem.url == newItem.url
        }
    }

    inner class NewsViewHolder(private val binding: CardArticleBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onNewsClick(getItem(position))
                }
            }
        }

        fun bind(model: ArticleModel) {
            binding.apply {
                Glide.with(imageI)
                    .load(model.urlToImage)
                    .into(imageI)
                sourceT.text = model.source?.name
                titleT.text = model.title
                descriptionT.text = model.description
                timestampT.text = model.publishedAt
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(CardArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnNewsListener {
        fun onNewsClick(article: ArticleModel)
    }
}