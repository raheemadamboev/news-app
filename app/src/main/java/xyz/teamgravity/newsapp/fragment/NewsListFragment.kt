package xyz.teamgravity.newsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.newsapp.api.Resource
import xyz.teamgravity.newsapp.databinding.FragmentNewsListBinding
import xyz.teamgravity.newsapp.helper.adapter.NewsAdapter
import xyz.teamgravity.newsapp.helper.constants.MyApi
import xyz.teamgravity.newsapp.helper.extensions.exhaustive
import xyz.teamgravity.newsapp.model.ArticleModel
import xyz.teamgravity.newsapp.viewmodel.NewsViewModel

@AndroidEntryPoint
class NewsListFragment : Fragment(), NewsAdapter.OnNewsListener {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewsViewModel>()

    private lateinit var adapter: NewsAdapter

    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView()
    }

    private fun recyclerView() {
        adapter = NewsAdapter(this)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.addOnScrollListener(onScrollListener)

            // events
            viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Success -> {
                        progressBar.visibility = View.INVISIBLE
                        isLoading = false
                        response.data?.let {
                            adapter.submitList(it.articles.toList())

                            val totalResult = it.totalResults / MyApi.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.breakingNewsPage == totalResult

                            if (isLastPage) recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }

                    is Resource.Error -> {
                        progressBar.visibility = View.INVISIBLE
                        isLoading = false
                        response.message?.let {
                            Snackbar.make(parentLayout, it, Snackbar.LENGTH_LONG).show()
                        }
                    }

                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        isLoading = true
                    }
                }.exhaustive
            }
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            // get layout manager
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            // get first visible item position
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            // get visible item count
            val visibleItemCount = layoutManager.childCount
            // get total item count
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= MyApi.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews()
                isScrolling = false
            }
        }
    }

    // news click
    override fun onNewsClick(article: ArticleModel) {
        findNavController().navigate(NewsListFragmentDirections.actionNewsListFragmentToArticleFragment(article))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}