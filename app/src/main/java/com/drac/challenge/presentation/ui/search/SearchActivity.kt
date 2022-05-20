package com.drac.challenge.presentation.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.drac.challenge.databinding.ActivityMainBinding
import com.drac.challenge.domain.model.Category
import com.drac.challenge.presentation.common.State
import com.drac.challenge.presentation.common.closeProgressDialog
import com.drac.challenge.presentation.common.hideInput
import com.drac.challenge.presentation.common.showProgressDialog
import com.drac.challenge.presentation.ui.results.ResultsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<SearchVM>()

    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel

        viewModel.addCallbacks()

        initAdapters()
        initListenersOrObservers()

        binding.rvCategories.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        viewModel.getCategories()
    }

    private fun initAdapters() {
        adapter = CategoryAdapter {
            goToResults(null, it)
        }
        binding.rvCategories.adapter = adapter
    }

    private fun initListenersOrObservers() {
        binding.btnRepeat.setOnClickListener {
            viewModel.getCategories()
        }

        viewModel.badInput.onEach {
            if(it) {
                Toast.makeText(this@SearchActivity, "Ingresar informacion", Toast.LENGTH_SHORT).show()
            }
        }.launchIn(lifecycleScope)

        viewModel.goToSearch.onEach {
            hideInput(binding.etSearch)
            goToResults(it, null)
        }.launchIn(lifecycleScope)

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

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.evaluateQuery()
            }
            true
        }
    }

    private fun hideOrShowRequestAgain(show: Boolean) {
        binding.layTryAgain.visibility = if(show) View.VISIBLE else View.GONE
    }

    private fun fillAdapter(lst: MutableList<Category>) {
        adapter.submitList(lst)
    }

    private fun goToResults(query: String?, category: Category? ) {
        ResultsActivity.startActivity(this, query,category?.id)
    }

}