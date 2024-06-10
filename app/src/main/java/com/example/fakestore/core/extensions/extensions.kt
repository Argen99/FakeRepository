package com.example.fakestore.core.extensions

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun ImageView.loadImageWithGlide(url: String) = Glide.with(this).load(url).centerInside().into(this)

fun Fragment.showShortToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Button.setDrawableEnd(context: Context, id: Int) {
    setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, id), null)
}