package com.example.blindhelper.data

data class Reward(
    var name:String,
    var store:String,
    var price:String,
    var id:Int
){
    constructor(): this("", "", "", 0)
}
