package xyz.teamgravity.newsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.newsapp.R
import xyz.teamgravity.newsapp.databinding.FragmentSavedNewsBinding
import xyz.teamgravity.newsapp.helper.adapter.NewsAdapter
import xyz.teamgravity.newsapp.model.ArticleModel
import xyz.teamgravity.newsapp.viewmodel.NewsViewModel

@AndroidEntryPoint
class SavedNewsFragment : Fragment(), NewsAdapter.OnNewsListener {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NewsViewModel>()

    private lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)

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

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.START or ItemTouchHelper.END
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val article = adapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteNews(article)
                    Snackbar.make(parentLayout, R.string.news_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo) {
                            viewModel.insertNews(article)
                        }.show()
                }
            }).attachToRecyclerView(recyclerView)

            // observer news
            viewModel.getNews().observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    // news card click
    override fun onNewsClick(article: ArticleModel) {
        findNavController().navigate(SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}