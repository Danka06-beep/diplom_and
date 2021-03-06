package com.kuzmin.nirvana

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kuzmin.nirvana.Adapter.PostAdapter
import com.kuzmin.nirvana.api.App
import com.kuzmin.nirvana.dto.PasswordChangeRequestDto
import com.kuzmin.nirvana.model.PostModel
import com.kuzmin.nirvana.other.Helper
import com.kuzmin.nirvana.other.isValidPassword
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_like.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.toolbar
import kotlinx.android.synthetic.main.item_tool_post.*
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private var attachmentModel: PostModel.AttachmentModel? = null
    val REQUEST_IMAGE_CAPTURE = 1
    private var  MY_PERMISSIONS_REQUEST_CAMERA = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        val id = intent.getStringExtra("id")
        ChangePasswordBtn.setOnClickListener {
            changePassw()
        }
        attachPhotoImgSetting.setOnClickListener {
            dispatchTakePictureIntent()
        }
        exitUser.setOnClickListener {
            getSharedPreferences(API_SHARED_FILE,Context.MODE_PRIVATE)
                .edit()
                .remove(AUTHENTICATED_SHARED_KEY)
                .apply()
            val intent = Intent(this@UserActivity, MainActivity::class.java)
            startActivity(intent)
        }
        }
    private fun changePassw(){
        lifecycleScope.launch {
            val password = passwordText.text?.toString().orEmpty()
            val twoPassword = passwordTwoText.text?.toString().orEmpty()
            val oldPassword = oldPasswordText.text?.toString().orEmpty()
            when {
                !isValidPassword(oldPassword) -> {
                    Toast.makeText(this@UserActivity, "???? ?????????????????? ????????????", Toast.LENGTH_SHORT).show()
                }
                !isValidPassword(password) -> {
                    Toast.makeText(this@UserActivity, "???? ?????????????????? ????????????", Toast.LENGTH_SHORT).show()
                }
                !(password == twoPassword) -> {
                    Toast.makeText(this@UserActivity, "???????????? ???? ??????????????????", Toast.LENGTH_SHORT).show()
                }
                password == "" -> {
                    Toast.makeText(this@UserActivity, "?????????????? ????????????", Toast.LENGTH_SHORT).show()
                }
                twoPassword == "" -> {
                    Toast.makeText(this@UserActivity, "?????????????? ???????????? ????????????????", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    dialog = ProgressDialog(this@UserActivity).apply {
                        setMessage(getString(R.string.please_wait))
                        Toast.makeText(this@UserActivity, R.string.loadData, Toast.LENGTH_SHORT).show()
                        show()
                        setCancelable(false)
                    }
                    try {
                        val author = App.repository.changePassword(PasswordChangeRequestDto(oldPassword,password))
                        dialog?.dismiss()
                        if (author.isSuccessful) {
                            Toast.makeText(this@UserActivity, "???????????? ??????????????", Toast.LENGTH_SHORT).show()
                            passwordText.setText("")
                            passwordTwoText.setText("")
                            oldPasswordText.setText("")
                        } else {
                            Toast.makeText(this@UserActivity, "???? ?????????????????? ????????????", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@UserActivity, R.string.error_occured, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun imageUploaded() {
        transparetAllIcons()
        attachPhotoImgSetting.visibility = View.VISIBLE
    }
    private fun transparetAllIcons() {
        imgSeting.setImageResource(R.drawable.ic_baseline_image_true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                lifecycleScope.launch {
                    dialog = ProgressDialog(this@UserActivity).apply {
                        setMessage(this@UserActivity.getString(R.string.please_wait))
                        setTitle(R.string.create_new_post)
                        setCancelable(false)
                        setProgressBarIndeterminate(true)
                        show()
                    }
                    val imageUploadResult =  App.repository.uploadImageUser(it)
                    Helper.mediaUploaded(PostModel.AttachmentType.IMAGE, this@UserActivity)
                    dialog?.dismiss()
                    if (imageUploadResult.isSuccessful) {
                        imageUploaded()
                        attachmentModel = imageUploadResult.body()
                    } else {
                        Toast.makeText(this@UserActivity, getString(R.string.error_upload), Toast.LENGTH_LONG).show()
                    }
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
                    Toast.makeText(this, "?????? ????????????????????", Toast.LENGTH_SHORT).show()
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
        }



