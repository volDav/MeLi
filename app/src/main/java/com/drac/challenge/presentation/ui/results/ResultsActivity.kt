package com.drac.challenge.presentation.ui.results

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.drac.challenge.databinding.ActivityResultsBinding
import com.drac.challenge.domain.model.Item
import com.drac.challenge.presentation.common.State
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

        viewModel.stateRequest.onEach {
            when (it) {
                is State.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                is State.Success -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
                is State.Error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }.launchIn(lifecycleScope)

        viewModel.loadRecycler.onEach {
            fillAdapter(it)
        }.launchIn(lifecycleScope)

        viewModel.executeQuery()
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