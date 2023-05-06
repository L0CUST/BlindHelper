package com.example.blindhelper.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.blindhelper.MainActivity
import com.example.blindhelper.ObstacleActivity
import com.example.blindhelper.data.Obstacle
import com.example.blindhelper.databinding.ObstacleDialogBinding
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ObstacleDialog(val obstacle:Obstacle, val uuuid:String, val point:Int) : DialogFragment(){
    val viewModel=ObstacleViewModel()
    private val database = Firebase.database
    private val userRef = database.getReference("user")
    lateinit var binding:ObstacleDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ObstacleDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            viewModel.postObstacle(obstacle)
            userRef.child(uuuid).child("point").setValue(point)
            (activity as ObstacleActivity).finish()
            dismiss()
        }
        super.onViewCreated(view, savedInstanceState)
    }
}