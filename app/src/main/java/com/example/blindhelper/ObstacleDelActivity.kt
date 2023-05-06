package com.example.blindhelper

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.blindhelper.data.Obstacle
import com.example.blindhelper.databinding.ActivityObstacleBinding
import com.example.blindhelper.databinding.ActivityObstacleDelBinding
import com.example.blindhelper.dialog.ObstacleDialog
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import java.util.UUID

class ObstacleDelActivity() : AppCompatActivity() {
    private val userModel : UserViewModel by viewModels()
    private val obstacleModel : ObstacleViewModel by viewModels()
    lateinit var binding: ActivityObstacleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObstacleBinding.inflate(layoutInflater)
        val latitude=127
        val longitude=36
        setContentView(binding.root)
        val uid = intent.getStringExtra("uid")
        val point = intent.getIntExtra("point", 0)
        binding.btnRegister.setOnClickListener {

            val obstacle = Obstacle(
                UUID.randomUUID().toString(),
                latitude,
                longitude,
                0,
                uid!!,
                binding.editExplain.text.toString()
            )
            obstacleModel.postObstacle(obstacle)


        }
    }
    fun fin() {
        finish()
    }
}