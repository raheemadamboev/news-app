package xyz.teamgravity.newsapp.model

data class NewsResponseModel(
    val articles: List<ArticleModel>,
    val status: String,
    val totalResults: Int
)