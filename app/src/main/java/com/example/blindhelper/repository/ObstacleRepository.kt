package com.example.blindhelper.repository

import androidx.lifecycle.MutableLiveData
import com.example.blindhelper.data.Obstacle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ObstacleRepository{
    private val database = Firebase.database
    private val obsRef = database.getReference("obstacle")
    lateinit var arr:ArrayList<Obstacle>
    //유저의 장애물 등록 현황 반환
    fun observeObstacleList(arrObs: MutableLiveData<ArrayList<Obstacle>>){

        obsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arr=ArrayList<Obstacle>()

                for(snap in snapshot.children){
                    val obs = snap.getValue(Obstacle::class.java)
                    obs?.let {
                        arr.add(obs)
                    }
                }
                arrObs.postValue(arr)
            }

            override fun onCancelled(error: DatabaseError) {
                //arr.add(Cafe("정보 없음",0,0F))
            }
        }
        )
    }
    fun postObstacle(obs:Obstacle){
        obsRef.child(obs.uuid).setValue(obs)
    }
    fun deleteObstacle(uuid:String) {
        obsRef.child(uuid).removeValue()
    }
}