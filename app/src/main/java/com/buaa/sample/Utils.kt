package com.buaa.sample

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

/**
 * create by xin on 2022-3-28
 */

const val KEY_URI = "KEY_URI"
const val REQUEST_GET_SINGLE_FILE = 100

const val LOG_TAG = "LOG"

fun printLog(msg: String) = Log.e(LOG_TAG, msg)

fun toast(context: Context, hint: String) =
    Toast.makeText(context, hint, Toast.LENGTH_SHORT).show()

fun snack(view: View, hint: String, listener: View.OnClickListener? = null) {
    val snack = Snackbar.make(view, hint, Snackbar.LENGTH_SHORT)
    if (listener != null)
        snack.setAction("action", listener)
    snack.show()
}

const val PHONE_REGEX =
    "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$"

fun isPhoneInvalidate(number: String) =
    !Pattern.compile(PHONE_REGEX).matcher(number).matches()

fun showContentDialog(context: Context, title: String, content: String) =
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(content)
        .setCancelable(true)
        .create()
        .show()


