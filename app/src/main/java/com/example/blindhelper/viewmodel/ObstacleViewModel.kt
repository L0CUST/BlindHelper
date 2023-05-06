package com.example.blindhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blindhelper.data.Obstacle
import com.example.blindhelper.repository.ObstacleRepository

class ObstacleViewModel():ViewModel() {
    private val repository = ObstacleRepository()
    private val _obstacles = MutableLiveData<ArrayList<Obstacle>>()
    val obstacles get():LiveData<ArrayList<Obstacle>> = _obstacles
    init {
        repository.observeObstacleList(_obstacles)
    }

    fun postObstacle(obstacle: Obstacle) {
        if (obstacle.has_obstacle == 1) {//장애물 등록
            repository.postObstacle(obstacle)

        } else {
            repository.deleteObstacle(obstacle.uuid)
        }
    }
}