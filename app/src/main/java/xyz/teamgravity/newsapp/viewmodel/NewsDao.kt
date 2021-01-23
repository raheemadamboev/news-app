package xyz.teamgravity.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.teamgravity.newsapp.helper.constants.MyDatabase
import xyz.teamgravity.newsapp.model.ArticleModel

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticleModel)

    @Delete
    suspend fun delete(article: ArticleModel)

    @Query("SELECT * FROM ${MyDatabase.ARTICLE_TABLE}")
    fun getNews(): LiveData<List<ArticleModel>>
}