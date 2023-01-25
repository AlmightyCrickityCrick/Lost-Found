package com.example.lostfound.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lostfound.R
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.data.user.LoginDataSource
import com.example.lostfound.data.user.LoginRepository
import com.example.lostfound.databinding.ActivityLoginBinding
import com.example.lostfound.databinding.ActivityOtpactivityBinding
import com.example.lostfound.utils.login.LoggedInUserView
import com.example.lostfound.utils.login.LoginViewModelFactory
import com.example.lostfound.utils.register.OTPViewModel
import com.example.lostfound.utils.register.RegisterViewModel

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var otpViewModel: OTPViewModel

    private  var username:String?=null
    private var otp : String?=null
    private var pass:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("USERNAME")
        otp = intent.getStringExtra("OTPId")
        pass = intent.getStringExtra("PASS")

        register(username!!, pass!!)

        otpViewModel = OTPViewModel()

        var input = binding.otpText
        var button = binding.otpSubmitButton

        button.setOnClickListener {
            if(otp!=null)otpViewModel.getResult(otp!!, input.text.toString())


        }

        otpViewModel.isSuccess.observe(this@OTPActivity, Observer {
            val success = it?:return@Observer
            if(success){
                if(username!=null && pass!=null) registerViewModel.register(username!!, pass!!)
            }
        })
    }

    private fun register(username:String, password:String){
         registerViewModel = ViewModelProvider(this, LoginViewModelFactory())
             .get(RegisterViewModel::class.java)

        registerViewModel.loginDataChanged(username, password)

        registerViewModel.loginResult.observe(this@OTPActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                setResult(RESULT_OK)
                intent = Intent(this, RegisterInformationActivity::class.java)
                intent.putExtra("USER", registerViewModel.getUserFromRepository())
                startActivity(intent)
                //Complete and destroy login activity once successful
                finish()
            }

        })
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}