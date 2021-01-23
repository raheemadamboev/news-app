package xyz.teamgravity.newsapp.model

data class NewsResponseModel(
    val articles: MutableList<ArticleModel>,
    val status: String,
    val totalResults: Int
)