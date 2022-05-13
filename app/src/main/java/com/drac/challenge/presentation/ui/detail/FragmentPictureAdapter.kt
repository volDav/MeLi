package com.drac.challenge.presentation.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.drac.challenge.domain.model.Picture

class FragmentPictureAdapter (fragmentActivity: FragmentActivity, private val lst : List<Picture>): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = lst.size

    override fun createFragment(position: Int): Fragment = FragmentPicture.newFragment(lst[position])

}