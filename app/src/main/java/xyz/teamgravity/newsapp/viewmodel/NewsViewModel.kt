package xyz.teamgravity.newsapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class NewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {



}