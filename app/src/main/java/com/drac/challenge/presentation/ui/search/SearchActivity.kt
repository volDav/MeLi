package com.drac.challenge.presentation.ui.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.drac.challenge.databinding.ActivityMainBinding
import com.drac.challenge.presentation.common.hideInput
import com.drac.challenge.presentation.ui.results.ResultsActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<SearchVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.viewModel = viewModel

        viewModel.badInput.onEach {
            if(it) {
                Toast.makeText(this@SearchActivity, "Ingresar informacion", Toast.LENGTH_SHORT).show()
            }
        }.launchIn(lifecycleScope)

        viewModel.goToSearch.onEach {
            hideInput(binding.etSearch)
            ResultsActivity.startActivity(this,it)
        }.launchIn(lifecycleScope)

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.evaluateQuery()
            }
            true
        }

    }

}