package com.drac.challenge.presentation.ui.results

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.drac.challenge.databinding.ActivityResultsBinding
import com.drac.challenge.domain.model.Item
import com.drac.challenge.presentation.common.State
import com.drac.challenge.presentation.common.closeProgressDialog
import com.drac.challenge.presentation.common.showProgressDialog
import com.drac.challenge.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ResultsActivity : AppCompatActivity() {

    companion object {
        fun startActivity(ctx: Context, query: String) {
            val intent = Intent(ctx, ResultsActivity::class.java)
            intent.putExtra("query", query)
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

        viewModel.setQueryData(intent.getStringExtra("query") ?: "")

        loadAdapter()

        initListeners()

        viewModel.executeQuery()
    }

    private fun hideOrShowRequestAgain(show: Boolean) {
        binding.btnRepeat.visibility = if(show) View.VISIBLE else View.GONE
    }

    private fun initListeners() {
        viewModel.stateRequest.onEach {
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
                else -> Unit
            }
        }.launchIn(lifecycleScope)

        viewModel.loadRecycler.onEach {
            fillAdapter(it)
        }.launchIn(lifecycleScope)

        binding.btnRepeat.setOnClickListener {
            viewModel.executeQuery()
        }
    }

    private fun loadAdapter() {
        adapter = ItemAdapter(::onClickItem)
        binding.rvItems.adapter = adapter
    }

    private fun fillAdapter(lst: List<Item>) {
        adapter.submitList(lst)
    }

    private fun onClickItem(item: Item) {
        DetailActivity.startActivity(this, item.id)
    }

}