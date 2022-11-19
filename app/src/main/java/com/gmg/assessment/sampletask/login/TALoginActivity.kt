package com.gmg.assessment.sampletask.login

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
import com.gmg.assessment.sampletask.databinding.TaActivityLoginBinding
import com.gmg.assessment.sampletask.sign_up.TASignUpActivity
import com.gmg.assessment.sampletask.todo_list.TATodoListActivity


class TALoginActivity : AppCompatActivity() {

    private lateinit var loginActivityBinding : TaActivityLoginBinding
    private val loginViewModel : TALoginViewModel by lazy {  ViewModelProvider(this)[TALoginViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding = TaActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginActivityBinding.root)
        initData()
        setListener()
        observeViewModel()
    }

    private fun initData() {
        if (loginViewModel.isUserLogin()) {
            navigateToTodoListScreen()
            return
        }
        val spannableStringBuilder = SpannableStringBuilder(getString(R.string.already_have_account))
        spannableStringBuilder.append(" ")
        val signUpMessage = getString(R.string.sign_up)
        spannableStringBuilder.append(signUpMessage)
        spannableStringBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(p0: View) {
                startActivity(Intent(this@TALoginActivity, TASignUpActivity::class.java))
            }

            override fun updateDrawState(textPaint : TextPaint) {
                super.updateDrawState(textPaint)
                textPaint.color = ContextCompat.getColor(this@TALoginActivity, R.color.violet)
                textPaint.isUnderlineText = false
            }
        }, spannableStringBuilder.length - signUpMessage.length, spannableStringBuilder.length, 0)
        loginActivityBinding.tvLoginInMessage.apply {
            text = spannableStringBuilder
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun setListener() {
        loginActivityBinding.apply {
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //will be implement later
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        loginViewModel.userEmail = it.toString()
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
                        loginViewModel.userPassword = it.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    //will be implement later
                }
            })

            btnLogin.setOnClickListener {
                loginViewModel.isUserDetailValid()
            }
        }
    }

    private fun observeViewModel() {
        with(loginViewModel) {
            isLoginSuccessfully.observe(this@TALoginActivity) { isLogin ->
                if (isLogin == true) {
                    navigateToTodoListScreen()
                }
            }

            userEmailFieldError.observe(this@TALoginActivity) {
                if (it != null) {
                    loginActivityBinding.etEmail.error = getString(it)
                }
            }

            userPasswordFieldError.observe(this@TALoginActivity) {
                if (it != null) {
                    loginActivityBinding.etPassword.error = getString(it)
                }
            }

            errorAlertMessage.observe(this@TALoginActivity) {
                if (it != null) {
                    showErrorAlertDialog(getString(it))
                }
            }
        }
    }

    private fun showErrorAlertDialog(message : String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(message)
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
}