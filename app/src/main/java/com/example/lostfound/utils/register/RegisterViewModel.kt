package com.example.lostfound.utils.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lostfound.R
import com.example.lostfound.data.user.LoginRepository
import com.example.lostfound.data.Result
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

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}