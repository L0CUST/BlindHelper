package com.example.blindhelper.data

import java.util.UUID

data class Obstacle(
    val uuid:String,
    var latitude:Int,
    var longitude:Int,
    var has_obstacle:Int,
    var user_uid:String,
    var content:String
){
    constructor():this(UUID.randomUUID().toString(),0,0,1,"","")
}