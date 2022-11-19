package com.gmg.assessment.sampletask.repository

import com.gmg.assessment.sampletask.R
import com.gmg.assessment.sampletask.common.TAApplication
import com.gmg.assessment.sampletask.model.TATodoTask
import com.gmg.assessment.sampletask.model.TAUserDetail
import com.gmg.assessment.sampletask.repository.local.TAPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by gowtham.muralivel on 06/11/22.
 */
object TARepository {

    private val preferences : TAPreferences by lazy { TAApplication.appInstance.preferences }

    fun registerUserDetail(email : String, password : String): Int? {
        val userData = preferences.getUserDetail(email)
        return if (userData == null) {
            TAUserDetail(email, password).let {
                val userDetail = Gson().toJson(it) //Json string
                preferences.setUserDetail(email, userDetail)
                preferences.setCurrentLoginUser(email)
            }
            null
        } else {
            R.string.user_register_already
        }
    }

    fun loginUser(email: String, password: String): Int? {
        val userData = preferences.getUserDetail(email)
        if (userData != null) {
            val typeToken = object : TypeToken<TAUserDetail>() {}.type
            val userDetail : TAUserDetail = Gson().fromJson(userData, typeToken)
            return if (password == userDetail.password) {
                preferences.setCurrentLoginUser(email)
                null
            } else {
                R.string.invalid_password
            }
        }
        return R.string.invalid_user
    }

    fun getCurrentUserEmail() : String? {
        return preferences.getCurrentLoginUser()
    }

    fun getTodoTaskList(email: String?) : MutableList<TATodoTask> {
        val todoList = mutableListOf<TATodoTask>()
        if (email != null) {
            preferences.getTaskDetails(email)?.let {
                val typeToken = object : TypeToken<List<TATodoTask>?>() {}.type
                val taskDetailData : List<TATodoTask>? = Gson().fromJson(it, typeToken)
                taskDetailData?.let { _taskDetail ->
                    todoList.addAll(_taskDetail)
                }
            }
        }
        return todoList
    }

    fun addTodoTaskList(todoTask : TATodoTask, currentUserEmail : String) {
        getTodoTaskList(email = currentUserEmail).apply {
            add(0, todoTask)
            Gson().toJson(this).let {
                preferences.setTaskDetails(taskDetailList = it, email = currentUserEmail)
            }
        }
    }

    fun logoutUser() {
        preferences.setCurrentLoginUser(null)
    }
}