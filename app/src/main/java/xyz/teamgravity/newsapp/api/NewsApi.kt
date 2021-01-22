package xyz.teamgravity.newsapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.teamgravity.newsapp.helper.constants.MyApi
import xyz.teamgravity.newsapp.model.NewsResponseModel

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = MyApi.NEWS_API_KEY
    ): Response<NewsResponseModel>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") query: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = MyApi.NEWS_API_KEY
    ): Response<NewsResponseModel>
}