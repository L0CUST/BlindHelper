package com.example.blindhelper

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.blindhelper.data.Obstacle
import com.example.blindhelper.databinding.ActivityObstacleBinding
import com.example.blindhelper.dialog.ObstacleDialog
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import java.util.UUID

class ObstacleActivity() : AppCompatActivity() {
    private val userModel : UserViewModel by viewModels()
    private val obstacleModel : ObstacleViewModel by viewModels()
    lateinit var binding:ActivityObstacleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObstacleBinding.inflate(layoutInflater)
        val latitude=intent.getDoubleExtra("latitude", 0.0)
        val longitude=intent.getDoubleExtra("longitude", 0.0)
        setContentView(binding.root)
        val uid = intent.getStringExtra("uid")
        val point = intent.getIntExtra("point", 0)

        binding.btnRegister.setOnClickListener {

            val obstacle = Obstacle(
                UUID.randomUUID().toString(),
                latitude,
                longitude,
                1,
                uid!!,
                binding.editExplain.text.toString()
            )
            val obstacleDialog = ObstacleDialog(obstacle, uid, point+20)
            obstacleDialog.show(supportFragmentManager, "dialog")


        }
    }
    fun fin() {
        finish()
    }
}