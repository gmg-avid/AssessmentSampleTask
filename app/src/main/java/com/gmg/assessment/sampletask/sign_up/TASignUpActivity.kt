package com.gmg.assessment.sampletask.sign_up

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.gmg.assessment.sampletask.R
import com.gmg.assessment.sampletask.databinding.TaActivitySignUpBinding
import com.gmg.assessment.sampletask.todo_list.TATodoListActivity

/**
 * Created by gowtham.muralivel on 06/11/22.
 */
class TASignUpActivity : AppCompatActivity() {

    private lateinit var signUpActivityBinding : TaActivitySignUpBinding
    private val signUpViewModel : TASignUpViewModel by lazy {  ViewModelProvider(this)[TASignUpViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpActivityBinding= TaActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpActivityBinding.root)
        initData()
        setListener()
        observeViewModel()
    }

    private fun initData() {
        val spannableStringBuilder = SpannableStringBuilder(getString(R.string.already_have_account))
        spannableStringBuilder.append(" ")
        val loginMessage = getString(R.string.login)
        spannableStringBuilder.append(loginMessage)
        spannableStringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(p0: View) {
                finish()
            }

            override fun updateDrawState(textPaint : TextPaint) {
                super.updateDrawState(textPaint)
                textPaint.color = ContextCompat.getColor(this@TASignUpActivity, R.color.violet)
                textPaint.isUnderlineText = false
            }
        }, spannableStringBuilder.length - loginMessage.length, spannableStringBuilder.length, 0)
        signUpActivityBinding.tvSignUpMessage.apply {
            text = spannableStringBuilder
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun observeViewModel() {
        with(signUpViewModel) {
            isRegisterSuccessfully.observe(this@TASignUpActivity) { isLogin ->
                if (isLogin == true) {
                    navigateToTodoListScreen()
                }
            }

            userEmailFieldError.observe(this@TASignUpActivity) {
                if (it != null) {
                    signUpActivityBinding.etEmail.error = getString(it)
                }
            }

            userPasswordFieldError.observe(this@TASignUpActivity) {
                if (it != null) {
                    signUpActivityBinding.etPassword.error = getString(it)
                }
            }

            errorAlertMessage.observe(this@TASignUpActivity) {
                if (it != null) {
                    showErrorAlertDialog(getString(it))
                }
            }
        }
    }

    private fun showErrorAlertDialog(alertMessage: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(alertMessage)
        alertDialog.setCancelable(true)
        alertDialog.setPositiveButton(getString(R.string.okay)) { dialog, id ->
            dialog.cancel()
        }
        alertDialog.create().show()
    }

    private fun navigateToTodoListScreen() {
        val intent = Intent(this, TATodoListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun setListener() {
        signUpActivityBinding.apply {
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //will be implement later
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        signUpViewModel.userEmail = it.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    //will be implement later
                }
            })

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //will be implement later
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        signUpViewModel.userPassword = it.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    //will be implement later
                }
            })

            btnLogin.setOnClickListener {
                signUpViewModel.isUserDetailValid()
            }
        }
    }
}