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
import com.example.lostfound.utils.register_info.InfoViewModel

class RegisterInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterInformationBinding
    private lateinit var infoViewModel: InfoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstName = binding.firstName
        val lastName = binding.lastName
        val phone = binding.phoneNumber
        val dateOfBirth = binding.dateOfBirth
        val btnCreateAccount = binding.createAcc

        val loginRepository = LoginRepository(LoginDataSource())
        loginRepository.setLoggedInUser(intent.getSerializableExtra("USER") as LoggedInUser)

        infoViewModel = InfoViewModel(loginRepository)
        infoViewModel.infoFormState.observe(this@RegisterInformationActivity, Observer {
            val infoState = it?:return@Observer
            btnCreateAccount.isEnabled = infoState.isDataValid

            if(infoState.usernameError!=null){
                Toast.makeText(this, infoState.usernameError, Toast.LENGTH_SHORT).show()
            } else if( infoState.dateError != null){
                Toast.makeText(this, infoState.dateError, Toast.LENGTH_SHORT).show()
            }
            else if(infoState.phoneError!= null){
                Toast.makeText(this, infoState.phoneError, Toast.LENGTH_SHORT).show()
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
                phone.text.toString())
        }

    }
}