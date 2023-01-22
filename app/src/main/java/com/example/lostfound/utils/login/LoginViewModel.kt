package com.example.lostfound.utils.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.lostfound.data.user.LoginRepository
import com.example.lostfound.data.Result

import com.example.lostfound.R
import com.example.lostfound.data.model.LoggedInUser
import kotlinx.coroutines.*

open class LoginViewModel(protected val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    protected val _loginResult = MutableLiveData<LoginResult>()
    open val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        runBlocking {
            val job = GlobalScope.async { loginRepository.login(username, password) }
            val result = job.await()

                if (result is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = result.data.email))
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
        }
    }

    fun getUserFromRepository(): LoggedInUser? {
        print(loginRepository.user)
        return loginRepository.user
    }

    open fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

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