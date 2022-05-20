package com.drac.challenge.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.drac.challenge.databinding.FragmentPictureBinding
import com.drac.challenge.domain.model.Picture
import com.drac.challenge.presentation.common.get
import com.drac.challenge.presentation.common.put

class FragmentPicture  : Fragment() {

    companion object{
        fun newFragment(picture: Picture): FragmentPicture {
            val fragment = FragmentPicture()
            fragment.arguments = Bundle()
            fragment.put(picture)
            return fragment
        }
    }

    val viewModel by activityViewModels<DetailVM>()

    private val picture by lazy {
        get(Picture::class.java)
    }

    lateinit var binding : FragmentPictureBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadImageFromUrl(binding.ivPicture, picture.url)
    }

}