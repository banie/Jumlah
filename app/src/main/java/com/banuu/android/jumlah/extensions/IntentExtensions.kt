package com.banuu.android.jumlah.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Created by banie on 2018-01-14.
 */

fun Intent.shareText(context: Context, text: String) {
  putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
  putExtra(android.content.Intent.EXTRA_TEXT, text)
  type = "text/plain"
  context.startActivity(Intent.createChooser(this, "Share text via"))
}

fun Intent.shareFile(context: Context, fileUri: Uri, fileName: String = "") {
  addFlags(
      Intent.FLAG_GRANT_READ_URI_PERMISSION)
  putExtra(android.content.Intent.EXTRA_SUBJECT, fileName)
  putExtra(Intent.EXTRA_STREAM, fileUri)
  type = "records/csv"
  context.startActivity(Intent.createChooser(this, "Share file via"))
}