package com.kuzmin.nirvana

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.kuzmin.nirvana.api.App
import com.kuzmin.nirvana.model.PostModel
import com.kuzmin.nirvana.other.Helper
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.launch
import java.io.IOException

class CreatePostActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    private var  MY_PERMISSIONS_REQUEST_CAMERA = 100
    private var dialog: ProgressDialog? = null
    private var attachmentModel: PostModel.AttachmentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        attachPhotoImg.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
            else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
            }


        }
        createPostBtn.setOnClickListener {
            lifecycleScope.launch {
                dialog = ProgressDialog(this@CreatePostActivity).apply {
                    setMessage(this@CreatePostActivity.getString(R.string.please_wait))
                    setTitle(R.string.create_new_post)
                    setCancelable(false)
                    setProgressBarIndeterminate(true)
                    show()
                }
                try {
                    val result = App.repository.createPost(contentEdt.text.toString(), attachmentModel)
                    if (result.isSuccessful) {
                        handleSuccessfullResult()
                    } else {
                        handleFailedResult()
                    }
                } catch (e: IOException) {
                    handleFailedResult()
                } finally {
                    dialog?.dismiss()
                }

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            MY_PERMISSIONS_REQUEST_CAMERA ->
                if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED){
                    dispatchTakePictureIntent()
                }
                else{
                    Toast.makeText(this, "Нет разрешения", Toast.LENGTH_SHORT).show()
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    private fun handleSuccessfullResult() {
        Toast.makeText(this@CreatePostActivity, getString(R.string.post_created_successfully), Toast.LENGTH_LONG).show()
        finish()
    }

    private fun handleFailedResult() {
        Toast.makeText(this@CreatePostActivity, getString(R.string.error_occured), Toast.LENGTH_LONG).show()
    }
    private fun transparetAllIcons() {
        attachPhotoImg.setImageResource(R.drawable.ic_baseline_inactive)
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    private fun imageUploaded() {
        transparetAllIcons()
        attachPhotoDoneImg.visibility = View.VISIBLE
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                lifecycleScope.launch {
                    dialog = ProgressDialog(this@CreatePostActivity).apply {
                        setMessage(this@CreatePostActivity.getString(R.string.please_wait))
                        setTitle(R.string.create_new_post)
                        setCancelable(false)
                        setProgressBarIndeterminate(true)
                        show()
                    }
                    val imageUploadResult =  App.repository.upload(it)
                    Helper.mediaUploaded(PostModel.AttachmentType.IMAGE, this@CreatePostActivity)
                    dialog?.dismiss()
                    if (imageUploadResult.isSuccessful) {
                        imageUploaded()
                        attachmentModel = imageUploadResult.body()
                    } else {
                        Toast.makeText(this@CreatePostActivity, getString(R.string.error_upload), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    }
}