package com.application.places.detail

import android.databinding.BindingAdapter
import android.util.Log
import android.widget.EditText

/**
 * Binding for [DetailsActivityFragment]
 */
object FragmentDetailsBinding {
    @BindingAdapter("android:selected")
    @JvmStatic
    fun setSelection(editText: EditText, position: Int) {
        Log.d("Test", "selected: $position")
        if (!editText.hasFocus()) {
            editText.setSelection(Math.min(editText.text.length,position))
        }
    }
}