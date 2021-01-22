package xyz.teamgravity.newsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import xyz.teamgravity.newsapp.api.Resource
import xyz.teamgravity.newsapp.databinding.FragmentSearchNewsBinding
import xyz.teamgravity.newsapp.helper.adapter.NewsAdapter
import xyz.teamgravity.newsapp.helper.extensions.exhaustive
import xyz.teamgravity.newsapp.model.ArticleModel
import xyz.teamgravity.newsapp.viewmodel.NewsViewModel

@AndroidEntryPoint
class SearchNewsFragment : Fragment(), NewsAdapter.OnNewsListener {
    companion object {
        /**
         * News search typing delay
         */
        const val SEARCH_NEWS_DELAY = 500L
    }

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewsViewModel>()

    private lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsAdapter(this)

        binding.apply {
            recyclerView.adapter = adapter

            viewModel.searchNews.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        progressBar.visibility = View.INVISIBLE
                        response.data?.let { adapter.submitList(it.articles) }
                    }

                    is Resource.Error -> {
                        progressBar.visibility = View.INVISIBLE
                        response.message?.let { println("debug: $it") }
                    }

                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }.exhaustive
            }

            // search
            var job: Job? = null
            searchField.addTextChangedListener { query ->
                job?.cancel()
                job = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                    delay(SEARCH_NEWS_DELAY)

                    if (!query.isNullOrBlank()) {
                        viewModel.searchNews(query.toString())
                    }
                }
            }
        }
    }

    // news card click
    override fun onNewsClick(article: ArticleModel) {
        findNavController().navigate(SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(article))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}