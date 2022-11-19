package com.gmg.assessment.sampletask.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gmg.assessment.sampletask.R
import com.gmg.assessment.sampletask.common.TAUtil
import com.gmg.assessment.sampletask.repository.TARepository

/**
 * Created by gowtham.muralivel on 06/11/22.
 */
class TALoginViewModel(application : Application) : AndroidViewModel(application) {

    var userEmail : String? = null
    var userPassword : String? = null

    val userEmailFieldError : MutableLiveData<Int> = MutableLiveData()
    val userPasswordFieldError : MutableLiveData<Int> = MutableLiveData()
    val errorAlertMessage : MutableLiveData<Int> = MutableLiveData()
    val isLoginSuccessfully : MutableLiveData<Boolean> = MutableLiveData()

    fun isUserLogin(): Boolean {
        return !TARepository.getCurrentUserEmail().isNullOrBlank()
    }

    fun isUserDetailValid() {
        var isDetailValid = true

        if (userEmail.isNullOrEmpty() || userEmail.isNullOrBlank()) {
            userEmailFieldError.postValue(R.string.email_is_not_empty)
            isDetailValid = false
        } else if (!TAUtil.isEmailValid(email = userEmail!!)) {
            userEmailFieldError.postValue(R.string.email_is_required_valid)
            isDetailValid = false
        }

        if (userPassword.isNullOrEmpty() || userPassword.isNullOrBlank()) {
            userPasswordFieldError.postValue(R.string.password_is_not_empty)
            isDetailValid = false
        }

        if (isDetailValid) {
            loginUser()
        }
    }

    private fun loginUser() {
        if (userEmail != null && userPassword != null) {
            TARepository.loginUser(userEmail!!, userPassword!!)?.let {
                errorAlertMessage.postValue(it)
            } ?: run {
                isLoginSuccessfully.postValue(true)
            }
        }
    }
}