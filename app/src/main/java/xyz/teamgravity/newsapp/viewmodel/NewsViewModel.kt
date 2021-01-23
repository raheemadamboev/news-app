package xyz.teamgravity.newsapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
import xyz.teamgravity.newsapp.api.Resource
import xyz.teamgravity.newsapp.injection.App
import xyz.teamgravity.newsapp.model.ArticleModel
import xyz.teamgravity.newsapp.model.NewsResponseModel

class NewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository,
    app: Application
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponseModel>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponseModel>> = MutableLiveData()
    var searchNewsPage = 1

    private var breakingNewsResponse: NewsResponseModel? = null
    private var searchNewsResponse: NewsResponseModel? = null

    init {
        getBreakingNews()
    }

    // get breaking news from api, network request
    fun getBreakingNews(countryCode: String = "us") = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    // search news from api, network request
    fun searchNews(query: String) = viewModelScope.launch {
        safeSearchNewsCall(query)
    }

    // safe breaking news call
    private suspend fun safeBreakingNewsCall(countryCode: String) {
        // loading
        breakingNews.postValue(Resource.Loading())

        try {
            if (hasInternetConnections()) {
                // requesting
                val response = repository.getBreakingNews(countryCode, breakingNewsPage)

                // post value
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    // safe search news call
    private suspend fun safeSearchNewsCall(query: String) {
        // loading
        searchNews.postValue(Resource.Loading())

        try {
            if (hasInternetConnections()) {
                // requesting
                val response = repository.searchForNews(query, searchNewsPage)

                // post value
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network failure"))
                else -> searchNews.postValue(Resource.Error("Conversion error"))
            }
        }
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
            when(response.code()) {
                429 -> Resource.Error("You made too many requests within a window of time and have been rate limited. Back off for a while")
                else -> Resource.Error(response.message())
            }
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
            when(response.code()) {
                429 -> Resource.Error("You made too many requests within a window of time and have been rate limited. Back off for a while")
                else -> Resource.Error(response.message())
            }
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

    @Suppress("DEPRECATION")
    private fun hasInternetConnections(): Boolean {
        val connectivityManager = getApplication<App>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }
}