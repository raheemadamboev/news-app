package xyz.teamgravity.newsapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import xyz.teamgravity.newsapp.api.Resource
import xyz.teamgravity.newsapp.model.ArticleModel
import xyz.teamgravity.newsapp.model.NewsResponseModel

class NewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponseModel>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponseModel>> = MutableLiveData()
    var searchNewsPage = 1

    var breakingNewsResponse: NewsResponseModel? = null
    var searchNewsResponse: NewsResponseModel? = null

    // get breaking news from api, network request
    fun getBreakingNews(countryCode: String = "us") = viewModelScope.launch {
        // loading
        breakingNews.postValue(Resource.Loading())

        // requesting
        val response = repository.getBreakingNews(countryCode, breakingNewsPage)

        // post value
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }


    // search news from api, network request
    fun searchNews(query: String) = viewModelScope.launch {
        // loading
        searchNews.postValue(Resource.Loading())

        // requesting
        val response = repository.searchForNews(query, searchNewsPage)

        // post value
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    // convert response to resource
    private fun handleBreakingNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {
        return if (response.isSuccessful) {
            if (response.body() != null) {

                // increment page
                breakingNewsPage++

                // if response null get response
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = response.body()
                } else {
                    breakingNewsResponse?.articles?.addAll(response.body()!!.articles)
                }

                Resource.Success(breakingNewsResponse!!)
            } else {
                Resource.Error("Response body is null!")
            }
        } else {
            Resource.Error(response.message())
        }
    }

    // convert response to resource
    private fun handleSearchNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {
        return if (response.isSuccessful) {
            if (response.body() != null) {

                // increment page
                searchNewsPage++

                // if response null get response
                if (searchNewsResponse == null) {
                    searchNewsResponse = response.body()
                } else {
                    searchNewsResponse?.articles?.addAll(response.body()!!.articles)
                }

                Resource.Success(searchNewsResponse!!)
            } else {
                Resource.Error("Response body is null!")
            }
        } else {
            Resource.Error(response.message())
        }
    }

    // insert news
    fun insertNews(article: ArticleModel) = viewModelScope.launch {
        repository.insertNews(article)
    }

    // delete news
    fun deleteNews(article: ArticleModel) = viewModelScope.launch {
        repository.deleteNews(article)
    }

    // get saved news
    fun getNews() = repository.getNews()
}