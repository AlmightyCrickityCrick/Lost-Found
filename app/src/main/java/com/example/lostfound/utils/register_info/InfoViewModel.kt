package com.example.lostfound.utils.register_info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lostfound.R
import com.example.lostfound.data.user.LoginRepository
import com.example.lostfound.data.Result
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.utils.login.LoggedInUserView
import com.example.lostfound.utils.login.LoginResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.*

class InfoViewModel(private val loginRepository: LoginRepository) :ViewModel(){
    private val _infoForm = MutableLiveData<InfoFormState>()
    val infoFormState: LiveData<InfoFormState> = _infoForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun setUserInfo(dateOfBirth: String, firstName: String, lastName:String, phoneNumber:String, key:String?) {
        // can be launched in a separate asynchronous job
        runBlocking {
            val job = GlobalScope.async { loginRepository.setUserInfo(dateOfBirth, firstName, lastName, phoneNumber, key) }
            val result = job.await()

            if (result is Result.Success) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = "$firstName $lastName"))
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun getUserFromRepository(): LoggedInUser? {
        print(loginRepository.user)
        return loginRepository.user
    }

    fun dataChanged(dateOfBirth: String, firstName: String, lastName:String, phoneNumber:String) {
        if (!isDateValid(dateOfBirth)) {
            _infoForm.value = InfoFormState(dateError = R.string.invalid_date)
        } else if (!isPhoneValid(phoneNumber)) {
            _infoForm.value = InfoFormState(phoneError = R.string.invalid_phone)
        } else if (!isUsernameValid(firstName, lastName)){
            _infoForm.value = InfoFormState(usernameError = R.string.invalid_username)
        }
        else {
            _infoForm.value = InfoFormState(isDataValid = true)
        }
    }
    private fun isUsernameValid(lastName: String, firstName: String):Boolean{
        return lastName.isNotEmpty() && firstName.isNotEmpty()
    }

    // A placeholder date validation check
    private fun isDateValid(dateOfBirth:String): Boolean {
        var isFormated =((dateOfBirth.count{it == '-'} == 2) && (dateOfBirth.indexOfFirst { it == '-'} == 4))
        if(!isFormated) return false
        return true
        //var isAgeCorrect= (dateOfBirth.subSequence(0, 4).toInt() < Calendar.getInstance().get(Calendar.YEAR)) && (dateOfBirth.subSequence(0, 4).toInt() > 1900)
        //var isMonthCorrect = (dateOfBirth.subSequence(5, 7).toInt() > 0) && (dateOfBirth.subSequence(5, 7).toInt() <13)
        //return isAgeCorrect && isMonthCorrect
    }

    // A placeholder phone validation check
    private fun isPhoneValid(phoneNumber: String): Boolean {
        return ((phoneNumber.length == 9) && (phoneNumber.get(0) == '0') && (phoneNumber.get(1) in "67"))
    }
}