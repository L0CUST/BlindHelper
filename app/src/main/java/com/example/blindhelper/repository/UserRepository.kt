package com.example.blindhelper.repository

import androidx.lifecycle.MutableLiveData
import com.example.blindhelper.data.Obstacle
import com.example.blindhelper.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val database = Firebase.database
    private val userRef = database.getReference("user")
    lateinit var user:User

    fun observeObstacleList(uUser:  MutableLiveData<User>, id:String) {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(snap in snapshot.children){
                    val obs = snap.getValue(User::class.java)
                    obs?.let {
                        if (it.uId == id) {
                            uUser.postValue(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        )
    }
    fun getPoint(uid:String, point: Int) {//point는 업데이트 된 값!
        userRef.child(uid).child("point").setValue(point)
    }
}