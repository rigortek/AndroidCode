package com.cw.childrenabc

import android.app.Activity
import android.content.Context
import android.widget.Toast

class ToastUtil {
    companion object {  // 静态方法
        var toast:Toast ?= null

        public fun showShort(context: Activity, content: String) {
            if (toast == null) {
                toast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
            } else {
                toast?.setText(content)
            }
            toast?.show()
        }

        public fun showLong(context: Activity, content: String) {
            if (toast == null) {
                toast = Toast.makeText(context, content, Toast.LENGTH_LONG)
            } else {
                toast?.setText(content)
            }
            toast?.show()
        }
    }
}