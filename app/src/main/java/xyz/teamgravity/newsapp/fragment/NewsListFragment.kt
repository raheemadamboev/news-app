package xyz.teamgravity.newsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import xyz.teamgravity.newsapp.api.Resource
import xyz.teamgravity.newsapp.databinding.FragmentNewsListBinding
import xyz.teamgravity.newsapp.helper.adapter.NewsAdapter
import xyz.teamgravity.newsapp.helper.extensions.exhaustive
import xyz.teamgravity.newsapp.model.ArticleModel
import xyz.teamgravity.newsapp.viewmodel.NewsViewModel

class NewsListFragment : Fragment(), NewsAdapter.OnNewsListener {

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewsViewModel>()

    private lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // request
        viewModel.getBreakingNews()

        adapter = NewsAdapter(this)

        binding.apply {
            recyclerView.adapter = adapter


            viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
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
        }
    }

    // news click
    override fun onNewsClick(article: ArticleModel) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}