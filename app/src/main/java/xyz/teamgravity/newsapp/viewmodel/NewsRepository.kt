package xyz.teamgravity.newsapp.viewmodel

import xyz.teamgravity.newsapp.api.NewsApi
import xyz.teamgravity.newsapp.model.ArticleModel
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val dao: NewsDao,
    private val api: NewsApi
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) = api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchForNews(query: String, pageNumber: Int) = api.searchForNews(query, pageNumber)

    suspend fun insertNews(article: ArticleModel) = dao.insert(article)

    suspend fun deleteNews(article: ArticleModel) = dao.delete(article)

    fun getNews() = dao.getNews()
}