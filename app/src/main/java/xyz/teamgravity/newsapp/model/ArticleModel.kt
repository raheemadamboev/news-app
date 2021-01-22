package xyz.teamgravity.newsapp.model

data class ArticleModel(
    val author: Any,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: SourceModel,
    val title: String,
    val url: String,
    val urlToImage: String
)