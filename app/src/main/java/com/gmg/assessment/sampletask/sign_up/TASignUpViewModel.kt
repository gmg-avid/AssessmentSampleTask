package com.gmg.assessment.sampletask.sign_up

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gmg.assessment.sampletask.R
import com.gmg.assessment.sampletask.common.TAUtil
import com.gmg.assessment.sampletask.repository.TARepository

/**
 * Created by gowtham.muralivel on 06/11/22.
 */
class TASignUpViewModel(application: Application) : AndroidViewModel(application) {

    var userEmail : String? = null
    var userPassword : String? = null

    val userEmailFieldError : MutableLiveData<Int> = MutableLiveData()
    val userPasswordFieldError : MutableLiveData<Int> = MutableLiveData()
    val errorAlertMessage : MutableLiveData<Int> = MutableLiveData()
    val isRegisterSuccessfully : MutableLiveData<Boolean> = MutableLiveData()

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
        } else if (!TAUtil.isPasswordValid(password = userPassword!!)) {
            userPasswordFieldError.postValue(R.string.password_is_required_valid)
            isDetailValid = false
        }

        if (isDetailValid) {
            registerUser()
        }
    }

    private fun registerUser() {
        if (userEmail != null && userPassword != null) {
            TARepository.registerUserDetail(userEmail!!, userPassword!!)?.let {
                errorAlertMessage.postValue(it)
            } ?: run {
                isRegisterSuccessfully.postValue(true)
            }
        }
    }
}