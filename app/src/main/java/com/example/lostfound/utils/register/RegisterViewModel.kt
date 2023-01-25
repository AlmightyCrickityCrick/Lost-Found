package com.example.lostfound.utils.register

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lostfound.R
import com.example.lostfound.data.user.LoginRepository
import com.example.lostfound.data.Result
import com.example.lostfound.utils.crypto.CryptographyService
import com.example.lostfound.utils.login.LoggedInUserView
import com.example.lostfound.utils.login.LoginResult
import com.example.lostfound.utils.login.LoginViewModel
import kotlinx.coroutines.*

class RegisterViewModel (loginRepository: LoginRepository) : LoginViewModel(loginRepository) {

    private val _loginForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _loginForm

    //override val _loginResult = MutableLiveData<LoginResult>()
    override val loginResult: LiveData<LoginResult> = _loginResult

    @DelicateCoroutinesApi
    fun register(username: String, password: String) {
        // can be launched in a separate asynchronous job
        runBlocking {
            val job = GlobalScope.async {
                loginRepository.register(username, password)
            }
            val result = job.await()
            if (result is Result.Success) {
                login(username, password)
               // _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.email))
            } else {
                _loginResult.value = LoginResult(error = R.string.register_failed)
            }
        }
        }

    fun tryEncryption(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            var hello = "Hello how are you!! I love you***^^^"
            var k = CryptographyService.generateAssymetricKey()
            var bye = CryptographyService.encryptAsym(k.first, hello)
            var x = CryptographyService.decryptAsym(k.second, bye)
            print("hello")
        }
    }


    override fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = RegisterFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = RegisterFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        var hasSpecialSymbols = false
        var special = "!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~"
        for(i in  special){
            if(i in password) hasSpecialSymbols = true
        }
        var upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var hasUpper = false
        for(i in  upper){
            if(i in password) hasUpper = true
        }
        return ((password.length >= 8) && hasUpper && hasSpecialSymbols)
    }
}