package com.lukevanoort.chall500px

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View

fun View.getActivity(): Activity? {
    var context: Context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = (context as ContextWrapper).baseContext
    }
    return null
}