package com.example.blindhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blindhelper.data.User
import com.example.blindhelper.repository.UserRepository

class UserViewModel() : ViewModel() {
    val repository = UserRepository()

    private val _users = MutableLiveData<User>(User())
    val users get():LiveData<User> = _users
    fun setUser(user:User) {
        repository.addUser(user)
        repository.observeObstacleList(_users, user.uId)
    }
    fun getPoint() {
        repository.getPoint(users.value?.uId!!, users.value?.point!!+20)
    }
}