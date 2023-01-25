package com.example.lostfound.activities

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast

import com.example.lostfound.R
import com.example.lostfound.databinding.ActivityRegisterBinding
import com.example.lostfound.utils.afterTextChanged
import com.example.lostfound.utils.crypto.CryptographyService
import com.example.lostfound.utils.login.LoggedInUserView
import com.example.lostfound.utils.login.LoginViewModelFactory
import com.example.lostfound.utils.register.OTPViewModel
import com.example.lostfound.utils.register.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var otpViewModel: OTPViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val passwordConfirm = binding.passwordConfirm
        val signup = binding.signup
        val login = binding.btnToLogin
        val loading = binding.loading

        otpViewModel = OTPViewModel()

        otpViewModel.OTPId.observe(this@RegisterActivity, Observer {
            val otpId = it?:return@Observer
            if(otpId!=null){
                setResult(RESULT_OK)
                intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("USERNAME", username.text.toString())
                intent.putExtra("PASS", password.text.toString())
                intent.putExtra("OTPId", otpId.toString())
                startActivity(intent)
                //Complete and destroy login activity once successful
                finish()
            }


        })
        registerViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // disable login button unless both username / password is valid
            signup.isEnabled = registerState.isDataValid

            if (registerState.usernameError != null) {
                username.error = getString(registerState.usernameError)
            }
            if (registerState.passwordError != null) {
                password.error = getString(registerState.passwordError)
            }
        })



        username.afterTextChanged {
            registerViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                registerViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        registerViewModel.register(
//                            username.text.toString(),
//                            password.text.toString()
//                        )
//                }
//                false
//            }

            signup.setOnClickListener {
                otpViewModel.sendOTP(username.text.toString())
                loading.visibility = View.VISIBLE
            }
        }

        login.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }


}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */