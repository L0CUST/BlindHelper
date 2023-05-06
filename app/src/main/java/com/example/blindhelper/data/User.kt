package com.example.blindhelper.data

data class User (
    var name: String,
    var email: String,
    var uId: String,
    var point:Int
){
    constructor(): this("", "", "", 0)
}