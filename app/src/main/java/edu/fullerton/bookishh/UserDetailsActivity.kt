package edu.fullerton.bookishh

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import edu.fullerton.bookishh.db.MyDatabaseRepository
import java.io.File

private const val TAG = "UserDetailsActivity"
class UserDetailsActivity: AppCompatActivity() {

    private lateinit var videoFile: File
    private lateinit var file: File
    private lateinit var videoUploadButton: Button
    private lateinit var fileUploadButton: Button
    private lateinit var videoView: VideoView
    private lateinit var videoDeleteButton: Button
    private lateinit var fileDeleteButton: Button
    private lateinit var submitButton: Button
    private lateinit var dbRepo: MyDatabaseRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.apply_job)

        videoUploadButton = this.findViewById(R.id.video_upload_button)
        fileUploadButton = this.findViewById(R.id.file_upload_button)
        videoView = this.findViewById(R.id.video_preview)
        videoDeleteButton = this.findViewById(R.id.video_delete_button)
        fileDeleteButton = this.findViewById(R.id.file_delete_button)
        submitButton = this.findViewById(R.id.submit_button)
        dbRepo = MyDatabaseRepository(application)

        // set click listener for video upload button
        videoUploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, REQUEST_CODE_VIDEO_UPLOAD)
        }

        // set click listener for file upload button
        fileUploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, REQUEST_CODE_VIDEO_UPLOAD)
        }

        // set click listener for video delete button
        videoDeleteButton.setOnClickListener {
            if (::videoFile.isInitialized) {
                videoFile.delete()
                videoView.stopPlayback()
                videoView.setVideoURI(null)
            }
        }

        // set click listener for file delete button
        fileDeleteButton.setOnClickListener {
            if (::file.isInitialized) {
                file.delete()
            }
        }

        // set click listener for submit button
        submitButton.setOnClickListener {
            // submit files
            dbRepo.addUserDetails("Saloni", videoFile.path, file.path)
            Toast.makeText(this, "Application Submitted", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_VIDEO_UPLOAD -> {
                    data?.data?.let { uri ->
                        videoView.visibility = View.VISIBLE
                        videoDeleteButton.visibility = View.VISIBLE
                        videoFile = getFileFromUri(uri)
                        videoView.setVideoURI(uri)
                        videoView.start()
                    }
                }
                REQUEST_CODE_FILE_UPLOAD -> {
                    data?.data?.let { uri ->
                        file = getFileFromUri(uri)
                        fileDeleteButton.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getFileFromUri(uri: Uri): File {
//        val filePath = getRealPathFromURI(uri)
//        Log.v(TAG, "KUCH $filePath")
        return File(uri.path)
    }

//    private fun getRealPathFromURI(uri: Uri): String? {
//        if(uri == null)
//            return null
//        val projection = arrayOf(MediaStore.Images        .Media.DATA)
//        val cursor = contentResolver.query(uri, projection, null, null, null)
//        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor?.moveToFirst()
//        val filePath = if (columnIndex != null && cursor != null) {
//            cursor.getString(columnIndex)
//        } else {
//            uri.path ?: ""
//        }
//        cursor?.close()
//        return filePath
//    }

    companion object {
        private const val REQUEST_CODE_VIDEO_UPLOAD = 1
        private const val REQUEST_CODE_FILE_UPLOAD = 2
    }
}