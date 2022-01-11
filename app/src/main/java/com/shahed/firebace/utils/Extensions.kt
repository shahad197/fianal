package com.shahed.firebace.utils

import android.text.TextUtils
import android.view.View
import android.widget.EditText

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun checkFieldsNotEmpty(vararg editTexts: EditText): Boolean {

    for (item in editTexts) {
        if (TextUtils.isEmpty(item.text)) {
            return false
        } else continue
    }
    return true
}
