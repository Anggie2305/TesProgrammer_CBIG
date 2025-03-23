package com.example.tes_cbig.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel (private val context: Context) : ViewModel() {
    private val authRepository = AuthRepository()

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> get() = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.login(email, password)
                _loginResult.value = response

                if (response.statusCode == 1) {
                    response.data.api_token?.let { token ->
                        SharedPrefManager.getInstance(context).saveToken(token)
                        Log.d("LoginViewModel", "Token disimpan: $token")
                    }
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResponse(-1, "Terjadi kesalahan: ${e.message}", UserData("", "", "", "", ""))
                Log.e("LoginViewModel", "Error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}