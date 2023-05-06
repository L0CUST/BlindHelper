package com.example.blindhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.blindhelper.data.Obstacle
import com.example.blindhelper.databinding.ActivityObstacleBinding
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import java.util.UUID

class ObstacleActivity : AppCompatActivity() {
    private val userModel : UserViewModel by viewModels()
    private val obstacleModel : ObstacleViewModel by viewModels()
    lateinit var binding:ActivityObstacleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObstacleBinding.inflate(layoutInflater)
        val latitude=127
        val longitude=36
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener {
            val obstacle = Obstacle(
                UUID.randomUUID().toString(),
                latitude,
                longitude,
                1,
                intent.getStringExtra("uid")!!,
                binding.editExplain.text.toString()
            )
            obstacleModel.postObstacle(obstacle)
        }
    }
}