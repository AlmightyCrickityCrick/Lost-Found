package com.example.lostfound.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.lostfound.data.user.LoginDataSource
import com.example.lostfound.data.user.LoginRepository
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.databinding.ActivityRegisterInformationBinding
import com.example.lostfound.utils.afterTextChanged
import com.example.lostfound.utils.crypto.CryptographyService
import com.example.lostfound.utils.register_info.InfoViewModel

class RegisterInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterInformationBinding
    private lateinit var infoViewModel: InfoViewModel
    private var publicKey: String?=null
    private val loginRepository = LoginRepository(LoginDataSource())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.generateKeys()
        val firstName = binding.firstName
        val lastName = binding.lastName
        val phone = binding.phoneNumber
        val dateOfBirth = binding.dateOfBirth
        val btnCreateAccount = binding.createAcc



        loginRepository.setLoggedInUser(intent.getSerializableExtra("USER") as LoggedInUser)

        infoViewModel = InfoViewModel(loginRepository)
        infoViewModel.infoFormState.observe(this@RegisterInformationActivity, Observer {
            val infoState = it?:return@Observer
            btnCreateAccount.isEnabled = infoState.isDataValid

            if(infoState.usernameError!=null){
                firstName.error = getString(infoState.usernameError)
            } else if( infoState.dateError != null){
                dateOfBirth.error = getString(infoState.dateError)
            }
            else if(infoState.phoneError!= null){
                phone.error= getString(infoState.phoneError)
            }
        })
        infoViewModel.loginResult.observe(this@RegisterInformationActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                Toast.makeText(this, loginResult.error, Toast.LENGTH_SHORT).show()
            }
            if (loginResult.success != null) {
                Toast.makeText(this, loginResult.success.displayName, Toast.LENGTH_SHORT).show()
//                setResult(Activity.RESULT_OK)
                intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER", infoViewModel.getUserFromRepository())
                startActivity(intent)
                //Complete and destroy login activity once successful
                finish()
            }


        })

        firstName.afterTextChanged {
            infoViewModel.dataChanged(
                dateOfBirth.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                phone.text.toString()
            )
        }

        lastName.afterTextChanged {
            infoViewModel.dataChanged(
                dateOfBirth.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                phone.text.toString()
            )
        }


        dateOfBirth.afterTextChanged {
            infoViewModel.dataChanged(
                dateOfBirth.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                phone.text.toString()
            )
        }
        phone.afterTextChanged {
            infoViewModel.dataChanged(
                dateOfBirth.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                phone.text.toString()
            )
        }

        btnCreateAccount.setOnClickListener {
            infoViewModel.setUserInfo(dateOfBirth.text.toString(),
                firstName.text.toString(),
                lastName.text.toString(),
                phone.text.toString(),
            publicKey)
        }

    }

    fun generateKeys(){
        var private = CryptographyService.getFromSharedPreference("PRIVATE ${loginRepository.user?.userId}", applicationContext)
        if(!private.isNullOrEmpty()) {
            this.publicKey = null
            return
        }
        var pair = CryptographyService.generateAssymetricKey()
        CryptographyService.saveToSharedPreference("PRIVATE ${loginRepository.user?.userId}", pair.second, applicationContext)
        publicKey = pair.first
    }
}