package xyz.teamgravity.newsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.newsapp.R
import xyz.teamgravity.newsapp.databinding.FragmentArticleBinding
import xyz.teamgravity.newsapp.viewmodel.NewsViewModel

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewsViewModel>()

    private val args by navArgs<ArticleFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView()
        button()
    }

    private fun webView() {
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(args.article.url)
        }
    }

    private fun button() {
        onInsert()
    }

    // insert button
    private fun onInsert() {
        binding.insertB.setOnClickListener {
            viewModel.insertNews(args.article)
            Snackbar.make(requireView(), R.string.news_saved, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}