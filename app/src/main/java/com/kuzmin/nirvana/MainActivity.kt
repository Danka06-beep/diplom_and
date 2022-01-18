package com.kuzmin.nirvana

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kuzmin.nirvana.other.isValidPassword
import com.kuzmin.nirvana.other.isValidUsername
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogin.setOnClickListener {
            when {
                !isValidUsername(enterLogin.text.toString()) -> {
                    Toast.makeText(this@MainActivity, R.string.ErorLogin, Toast.LENGTH_SHORT).show()
                }
                !isValidPassword(enterPassword.text.toString()) -> {
                    Toast.makeText(this@MainActivity, R.string.ErorPassword, Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    lifecycleScope.launch {
                        dialog = ProgressDialog(this@MainActivity).apply {
                        }

                    }

                }
            }
        }
        btnRegistration.setOnClickListener {

        }
    }

    private fun goToPost() {
        if (authenticated()) {
            val intent = Intent(this@MainActivity, PostActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun authenticated(): Boolean =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY,
            "")?.isNotEmpty() ?: false


    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).edit()
            .putString(AUTHENTICATED_SHARED_KEY, token).apply()

    }
