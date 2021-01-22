package xyz.teamgravity.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.teamgravity.newsapp.helper.constants.MyDatabase

@Entity(tableName = MyDatabase.ARTICLE_TABLE)
data class ArticleModel(

    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: SourceModel,
    val title: String,
    val url: String,
    val urlToImage: String,

    @PrimaryKey(autoGenerate = true)
    val _id: Long? = null
)