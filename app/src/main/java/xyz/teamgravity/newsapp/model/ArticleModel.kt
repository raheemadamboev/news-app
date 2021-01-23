package xyz.teamgravity.newsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.teamgravity.newsapp.helper.constants.MyDatabase
import java.io.Serializable

@Entity(tableName = MyDatabase.ARTICLE_TABLE)
data class ArticleModel(

    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: SourceModel?,
    val title: String?,

    @PrimaryKey(autoGenerate = false)
    val url: String = "noUrl",

    val urlToImage: String?
) : Serializable