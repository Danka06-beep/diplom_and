package com.kuzmin.nirvana.other

import android.content.Context
import com.kuzmin.nirvana.API_SHARED_FILE
import java.util.regex.Pattern

const val LAST_TIME_VISIT_SHARED_KEY = "lastTimeVisitSharedKey"
fun isValidPassword(password: String) =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,})\$").matcher(password).matches()
fun isValidUsername(username: String) =
    Pattern.compile("(.{1,10})\$").matcher(username).matches()
fun isFirstTime(context: Context) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getLong(
        LAST_TIME_VISIT_SHARED_KEY, 0
    ) == 0L

fun setNotFirstTime(context: Context) =
    context.getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(API_SHARED_FILE, false)