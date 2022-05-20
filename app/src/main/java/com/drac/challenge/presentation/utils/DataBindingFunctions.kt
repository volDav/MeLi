package com.drac.challenge.presentation.utils

import android.os.Build
import android.text.Html
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("setText")
fun setText(textView : TextView, text: String) {
    HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        textView.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        textView.text = Html.fromHtml(text)
    }
}

@BindingAdapter("setPrice")
fun TextView.setPrice(text: Long) {
    setText(this, toPrice(text))
}

