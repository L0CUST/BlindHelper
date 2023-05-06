package com.example.blindhelper.data

data class Obstacle(
    var latitude:Double,
    var longitude:Double,
    var has_obstacle:Int,
    var user_uid:String,
    var content:String
){
    constructor():this(0.0,0.0,1,"","")
}