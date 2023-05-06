package com.example.blindhelper.data

data class Obstacle(
    var latitude:Int,
    var longitude:Int,
    var has_obstacle:Int,
    var user_uid:String,
    var content:String
){
    constructor():this(0,0,1,"","")
}