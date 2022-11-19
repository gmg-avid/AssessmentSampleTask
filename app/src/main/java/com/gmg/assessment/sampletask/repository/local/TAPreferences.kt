package com.gmg.assessment.sampletask.repository.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by gowtham.muralivel on 06/11/22.
 */
class TAPreferences(private val context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "todo_task_preference"
        private const val CURRENT_LOGIN_USER = "currentLoginUser"
        private const val TASK_DETAIL_LIST = "taskDetailList"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val sharedPreferencesEditor = sharedPreferences.edit()

    fun setUserDetail(email : String, userDetail : String): Boolean {
        sharedPreferences.getString(email, null).let {
            if (it != null) {
                return false
            } else {
                sharedPreferencesEditor?.apply {
                    putString(email, userDetail)
                    apply()
                }
                return true
            }
        }
    }

    fun setCurrentLoginUser(email: String?) {
        sharedPreferencesEditor?.apply {
            putString(CURRENT_LOGIN_USER, email)
            apply()
        }
    }

    fun setTaskDetails(taskDetailList : String, email: String) {
        val taskKey = TASK_DETAIL_LIST + email
        sharedPreferencesEditor?.apply {
            putString(taskKey, taskDetailList)
            apply()
        }
    }

    fun getTaskDetails(email: String): String? {
        val taskKey = TASK_DETAIL_LIST + email
        return sharedPreferences.getString(taskKey, null)
    }

    fun getCurrentLoginUser(): String? {
        return sharedPreferences.getString(CURRENT_LOGIN_USER, null)
    }

    fun getUserDetail(email: String): String? {
       return sharedPreferences.getString(email, null)
    }
}