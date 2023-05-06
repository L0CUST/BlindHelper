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
import com.example.blindhelper.repository.ObstacleRepository
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import java.util.UUID

class ObstacleDelActivity() : AppCompatActivity() {
    private val userModel : UserViewModel by viewModels()
    private val obstacleModel : ObstacleViewModel by viewModels()
    lateinit var binding:ActivityObstacleBinding
    private val repository=ObstacleRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        //마커를 클릭하면 해당 uuid를 인텐트에 넣기
        //1. uuid 2. content
        super.onCreate(savedInstanceState)
        binding = ActivityObstacleBinding.inflate(layoutInflater)
        val uuid=intent.getStringExtra("uuid")
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener {
            repository.deleteObstacle(uuid!!)

        }
    }
    fun fin() {
        finish()
    }
}