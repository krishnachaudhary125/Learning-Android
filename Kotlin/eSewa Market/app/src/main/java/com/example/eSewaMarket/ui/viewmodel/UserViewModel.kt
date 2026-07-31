package com.example.eSewaMarket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.UserResponse
import com.example.eSewaMarket.data.models.UserSyncRequest
import com.example.eSewaMarket.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun syncUser(
        token: String,
        request: UserSyncRequest
    ) {
        viewModelScope.launch {

            _loading.value = true

            try {

                val response = repository.syncUser(token, request)

                if (response.isSuccessful && response.body() != null) {
                    _user.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()

                    Log.e(
                        "UserSync",
                        "Code: ${response.code()}, Error: $errorBody"
                    )

                    _error.value = "Code: ${response.code()}\n$errorBody"
                }

            } catch (e: Exception) {
                Log.e("UserSync", "Sync failed", e)
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }
}