package com.drac.challenge.presentation.ui.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.drac.challenge.databinding.ActivityDetailBinding
import com.drac.challenge.domain.model.Item
import com.drac.challenge.presentation.common.State
import com.drac.challenge.presentation.common.closeProgressDialog
import com.drac.challenge.presentation.common.showProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        fun startActivity(ctx: Context, itemId: String) {
            val intent = Intent(ctx, DetailActivity::class.java)
            intent.putExtra("itemId", itemId)
            ctx.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailVM>()

    private var pageCallback : ViewPager2.OnPageChangeCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.setId(intent.getStringExtra("itemId") ?: "")

        initListeners()

        viewModel.executeRequest()

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

        viewModel.fullItem.observe(this) {
            setData(it)
            loadData(it)
        }

        binding.btnRepeat.setOnClickListener {
            viewModel.executeRequest()
        }
    }

    private fun hideOrShowRequestAgain(show: Boolean) {
        binding.btnRepeat.visibility = if(show) View.VISIBLE else View.GONE
    }

    private fun setData(it: Item) {
        binding.tvTitle.text = it.title
        binding.tvPrice.text = "${it.price}"
        binding.tvDescription.text = it.description?.plainText ?: ""
    }

    private fun loadData(item: Item) {
        item.pictures?.let { lst ->
            binding.vpCarrusel.adapter = FragmentPictureAdapter(this, lst)
            pageCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    if(lst.isNotEmpty()) {
                        val str = "${position + 1}/${lst.size}"
                        binding.tvPager.text = str
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            }
            pageCallback?.let {
                binding.vpCarrusel.registerOnPageChangeCallback(it)
            }
        }
    }

    override fun onDestroy() {
        pageCallback?.let {
            binding.vpCarrusel.unregisterOnPageChangeCallback(it)
        }
        super.onDestroy()
    }
}