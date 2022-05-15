package com.drac.challenge.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.DialogFragment
import com.drac.challenge.R
import com.drac.challenge.databinding.DialogProgressBinding

class ProgressDialog : DialogFragment() {

    var message: String = ""

    lateinit var binding: DialogProgressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogProgressBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.message = message
    }

}

fun FragmentActivity.showProgressDialog(message: String? = null) {
    val dialog = ProgressDialog()
    dialog.message = message ?: getString(R.string.loading)
    dialog.show(this.supportFragmentManager, ProgressDialog::class.java.name)

}

fun FragmentActivity.closeProgressDialog() {
    val progressDialog = supportFragmentManager.findFragmentByTag(ProgressDialog::class.java.name)
    if (progressDialog is ProgressDialog) {
        try {
            progressDialog.dismissAllowingStateLoss()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}