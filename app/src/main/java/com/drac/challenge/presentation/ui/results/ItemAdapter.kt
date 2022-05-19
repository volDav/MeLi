package com.drac.challenge.presentation.ui.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drac.challenge.databinding.HolderItemBinding
import com.drac.challenge.domain.model.Item
import com.drac.challenge.presentation.common.DiffCallback
import com.drac.challenge.presentation.common.loadImageFromUrl

class ItemAdapter(private val callback: (Item) -> Unit) : ListAdapter<Item, ItemAdapter.HolderItem>(
    DiffCallback(
        { o,n -> o == n },
        { o,n -> o.id == n.id }
    )
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderItem {
        return HolderItem(
            HolderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HolderItem, position: Int) {
        holder.onBind(getItem(position), callback) {
            notifyItemChanged(position)
        }
    }

    class HolderItem(private val binding: HolderItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(model: Item, callback: (Item) -> Unit, updateFavorite: () -> Unit) {
            binding.model = model
            binding.layItem.setOnClickListener {
                callback.invoke(model)
            }
            binding.imgFavorite.setOnClickListener {
                model.favorite = !model.favorite
                updateFavorite()
            }
            binding.ivPicture.loadImageFromUrl(model.thumbnail)
        }
    }

}