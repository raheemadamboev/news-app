package xyz.teamgravity.newsapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import xyz.teamgravity.newsapp.api.Resource
import xyz.teamgravity.newsapp.model.NewsResponseModel

class NewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponseModel>> = MutableLiveData()
    var breakingNewsPage = 1

    // get breaking news from api, network request
    fun getBreakingNews(countryCode: String = "uz") = viewModelScope.launch {
        // loading
        breakingNews.postValue(Resource.Loading())

        // requesting
        val response = repository.getBreakingNews(countryCode, breakingNewsPage)

        // post value
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }


    // convert response to resource
    private fun handleBreakingNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {
        return if (response.isSuccessful) {
            if (response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Response body is null!")
            }
        } else {
            Resource.Error(response.message())
        }
    }
}