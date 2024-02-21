package edu.fullerton.bookishh

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

private const val TAG = "AboutUsPopup"
class AboutUsPopup: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.v(TAG, "Popup is shown")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.about_job_board)
        builder.setMessage(R.string.about_page_content)
        builder.setNegativeButton(R.string.cancel) {_, _ -> dismiss() }
        return builder.create()
    }

}