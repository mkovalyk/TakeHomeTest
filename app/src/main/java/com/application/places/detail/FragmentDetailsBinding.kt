package com.application.places.detail

import android.databinding.BindingAdapter
import android.widget.EditText

/**
 * Binding for [DetailsActivityFragment]
 */
object FragmentDetailsBinding {
    @BindingAdapter("android:selected")
    @JvmStatic
    fun setSelection(editText: EditText, position: Int) {
        if (!editText.hasFocus()) {
            editText.setSelection(Math.min(editText.text.length,position))
        }
    }
}