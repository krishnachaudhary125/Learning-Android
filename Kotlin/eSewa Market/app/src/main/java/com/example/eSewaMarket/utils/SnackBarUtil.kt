package com.example.eSewaMarket.utils

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.eSewaMarket.R
import com.google.android.material.snackbar.Snackbar

object SnackBarUtil {

    fun show(
        view: View,
        context: Context,
        text: String,
        anchorView: View? = null,
        actionText: String? = null,
        action: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)

        anchorView?.let { snackbar.setAnchorView(it) }

        snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.black))
        snackbar.setTextColor(ContextCompat.getColor(context, R.color.white))
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.green))
        snackbar.view.setPadding(
            snackbar.view.paddingStart,
            8,
            snackbar.view.paddingEnd,
            8
        )

        if (actionText != null && action != null) {
            snackbar.setAction(actionText) {
                action()
            }
        }

        snackbar.show()
    }
}