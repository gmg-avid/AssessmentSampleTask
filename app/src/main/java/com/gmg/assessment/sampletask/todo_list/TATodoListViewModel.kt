package com.gmg.assessment.sampletask.todo_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gmg.assessment.sampletask.model.TATodoTask
import com.gmg.assessment.sampletask.repository.TARepository

/**
 * Created by gowtham.muralivel on 13/11/22.
 */
class TATodoListViewModel(application: Application) : AndroidViewModel(application) {

    var todoTaskList : MutableList<TATodoTask> = mutableListOf()
    var userEmail : String? = null

    val isUserEmailUpdate : MutableLiveData<String?> = MutableLiveData()
    val isTaskDetailUpdated : MutableLiveData<Boolean> = MutableLiveData()

    fun fetchInitialData() {
        userEmail = TARepository.getCurrentUserEmail()
        isUserEmailUpdate.value = userEmail
        getTaskList()
    }

    fun getTaskList() {
        TARepository.getTodoTaskList(userEmail).let {
            todoTaskList = it
        }
        isTaskDetailUpdated.value = true
    }

    fun logoutUser() {
        TARepository.logoutUser()
    }
}