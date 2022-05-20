package com.drac.challenge.presentation.ui.results

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drac.challenge.databinding.ActivityResultsBinding
import com.drac.challenge.domain.model.Item
import com.drac.challenge.presentation.common.State
import com.drac.challenge.presentation.common.closeProgressDialog
import com.drac.challenge.presentation.common.showProgressDialog
import com.drac.challenge.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ResultsActivity : AppCompatActivity() {

    companion object {
        fun startActivity(ctx: Context, query: String? = "", category: String? = "") {
            val intent = Intent(ctx, ResultsActivity::class.java)
            intent.putExtra("query", query)
            intent.putExtra("category", category)
            ctx.startActivity(intent)
        }
    }

    private val viewModel by viewModels<ResultVM>()

    private lateinit var binding: ActivityResultsBinding

    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadTypeOfConsult()
        loadAdapter()
        initListenersOrObservers()

        binding.rvItems.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadTypeOfConsult() {
        intent.getStringExtra("query")?.takeIf { it.isNotEmpty() }?.let {
            viewModel.setQueryData(it)
            return
        }
        intent.getStringExtra("category")?.takeIf { it.isNotEmpty() }?.let {
            viewModel.setCategoryData(it)
        }
    }

    private fun hideOrShowRequestAgain(show: Boolean) {
        binding.btnRepeat.visibility = if(show) View.VISIBLE else View.GONE
    }

    private fun initListenersOrObservers() {
        viewModel.stateRequest
            .onEach {
                when (it) {
                    is State.Loading -> {
                        showProgressDialog()
                        hideOrShowRequestAgain(false)
                    }
                    is State.Success -> {
                        closeProgressDialog()
                        hideOrShowRequestAgain(false)
                    }
                    is State.Error -> {
                        closeProgressDialog()
                        hideOrShowRequestAgain(true)
                    }
                    else -> Unit }
            }.launchIn(lifecycleScope)

        viewModel.loadRecycler.observe(this) {
            fillAdapter(it.toMutableList())
        }

        binding.btnRepeat.setOnClickListener {
            viewModel.executeQuery()
        }

        binding.rvItems.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager ->

                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    viewModel.evaluatePaging(
                        visibleItemCount,
                        totalItemCount,
                        firstVisibleItemPosition
                    )
                }
            }
        })
    }

    private fun loadAdapter() {
        adapter = ItemAdapter(::onClickItem)
        binding.rvItems.adapter = adapter
    }

    private fun fillAdapter(lst: MutableList<Item>) {
        adapter.submitList(lst)
    }

    private fun onClickItem(item: Item) {
        DetailActivity.startActivity(this, item.id)
    }

}