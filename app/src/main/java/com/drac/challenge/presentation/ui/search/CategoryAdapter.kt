package com.drac.challenge.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drac.challenge.databinding.HolderCategoryBinding
import com.drac.challenge.domain.model.Category
import com.drac.challenge.presentation.common.DiffCallback

class CategoryAdapter(private val callback: (p0 : Category) -> Unit) : ListAdapter<Category, CategoryAdapter.HolderCategory>(
    DiffCallback<Category>(
        { old,new -> old == new },
        { old,new -> old.id == new.id }
    )
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        return HolderCategory(
            HolderCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        holder.onBind(getItem(position),callback)
    }

    class HolderCategory(private val binding : HolderCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(category : Category, onClickCategory: (p0 : Category) -> Unit) {
            binding.model = category
            binding.root.setOnClickListener {
                onClickCategory.invoke(category)
            }
        }
    }

}