package com.example.blindhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blindhelper.data.User

class UserViewModel() : ViewModel() {


    private val _users = MutableLiveData<User>(User())
    val users get():LiveData<User> = users
    fun setUser(user: User) {
        _users.postValue(user)
    }
}