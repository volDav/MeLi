package com.drac.challenge.presentation.common

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

fun FragmentActivity.hideInput(view: View) {
     (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
}

class DiffCallback<K>(val compareItems: (old: K, new: K) -> Boolean, val compareContents: (old: K, new: K) -> Boolean) : DiffUtil.ItemCallback<K>() {
     override fun areItemsTheSame(old: K, new: K) = compareItems(old, new)
     override fun areContentsTheSame(old: K, new: K) = compareContents(old, new)
}


fun AppCompatImageView.loadImageFromUrl(url: String, onSuccess: () -> Unit = {}, onError: (p0 : String) -> Unit = {}) {
     if(url.isEmpty()) {
          onError.invoke("")
          return
     }
     Picasso.get()
          .load(url)
          .into(this,object: Callback {
               override fun onSuccess() {
                    onSuccess.invoke()
               }

               override fun onError(e: Exception?) {
                    onError.invoke(e?.message ?: "")
               }
          })
}