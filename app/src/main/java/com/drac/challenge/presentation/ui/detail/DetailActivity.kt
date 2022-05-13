package com.drac.challenge.presentation.ui.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.drac.challenge.databinding.ActivityDetailBinding
import com.drac.challenge.domain.model.Item
import com.drac.challenge.presentation.common.State
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

        viewModel.fullItem.observe(this) {
            binding.tvTitle.text = it.title
            binding.tvPrice.text = "${it.price}"
            loadData(it)
        }

        viewModel.executeRequest()

    }

    private fun loadData(item: Item) {
        item.pictures?.let {
            binding.vpCarrusel.adapter = FragmentPictureAdapter(this, it)
            pageCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    if(it.isNotEmpty()) {
                        val str = (position + 1).toString() + "/" + it.size
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