package xyz.teamgravity.newsapp.viewmodel

import xyz.teamgravity.newsapp.api.NewsApi
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val dao: NewsDao,
    private val api: NewsApi
) {



}