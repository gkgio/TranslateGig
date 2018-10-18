package com.gkgio.translate.helpers.utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import com.gkgio.translate.R
import java.util.*
import kotlin.collections.HashMap

fun Activity.snackBar(text: String) = Snackbar.make(this.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT)

fun Fragment.snackBar(text: String) {
  val activity = this.activity
  if (activity != null) {
    Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT)
  }
}

fun showErrorAlertDialog(
    activity: Activity?,
    message: String,
    positiveCallback: DialogInterface.OnClickListener) {
  if (activity != null) {
    android.support.v7.app.AlertDialog.Builder(activity)
        .setMessage(message)
        .setPositiveButton(R.string.yes, positiveCallback)
        .create()
        .show()
  } else {
    Log.e(TAG, "Can't show dialog without activity")
  }
}

fun isEmptyString(string: String?): Boolean {
  return string == null || string.isEmpty()
}

fun giveHashMapRandomElement(mapLanguages: HashMap<String, String>): String? {
  val languagesList = mapLanguages.keys.toTypedArray()
  val key = languagesList[Random().nextInt(languagesList.size)]
  return mapLanguages[key]
}