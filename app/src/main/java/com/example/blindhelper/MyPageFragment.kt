package com.example.blindhelper

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.blindhelper.databinding.FragmentMyPageBinding
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import com.kakao.sdk.user.UserApiClient

class MyPageFragment : Fragment() {
    private lateinit var binding:FragmentMyPageBinding
    private val model : UserViewModel by activityViewModels()
    private val obstacle : ObstacleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "로그인 안 함", error)
                Toast.makeText(context, "로그인을 한 사용자만 접근할 수 있습니다.",  Toast.LENGTH_SHORT).show()
                findNavController(this).navigate(R.id.action_myPageFragment_to_mapFragment)
            }
        }

        binding= FragmentMyPageBinding.inflate(inflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val users = model.users.value!!
        binding.txtName.text = users.name
        binding.txtPoint.text = users.point.toString()
        binding.txtEmail.text=users.email
        binding.btnLogout.setOnClickListener {
            kakaologout()
        }

    }
    private fun kakaologout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Log.i(ContentValues.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                Toast.makeText(context,"카카오 로그아웃 성공", Toast.LENGTH_SHORT).show()
                findNavController(this).navigate(R.id.action_myPageFragment_to_mapFragment)

            }
        }
    }

}