package com.banuu.android.jumlah.util

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by banie on 2018-01-14.
 */

class RecordUtil {
  companion object {
    private val RECORDS_DIR = "records"
    private val FILENAME_PREFIX = "Record for"
    private val DATE_FORMAT = "MMMM dd, yyyy hh:mm a"

    fun saveRecordToFile(context: Context, record: String,
                         recordName: String = makeCsvFilename()): File {
      val recordsDir = File(context.filesDir,
                            RECORDS_DIR)
      val recordFile = File(recordsDir, recordName)

      // create the directory 1st if it doesn't exist otherwise writing to the file will crash
      if (!recordsDir.exists()) {
        recordsDir.mkdir()
      }

      recordFile.writeText(record)
      return recordFile
    }

    fun makeCsvFilename(): String {
      return makeFilename() + ".csv"
    }

    fun makeFilename(): String {
      val locale = Locale.getDefault()
      val sdf = SimpleDateFormat(DATE_FORMAT, locale)

      sdf.timeZone = TimeZone.getDefault()

      return FILENAME_PREFIX + " " + sdf.format(Date())
    }
  }
}