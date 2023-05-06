package com.example.blindhelper

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.blindhelper.data.User
import com.example.blindhelper.databinding.ActivityMain2Binding
import com.example.blindhelper.viewmodel.UserViewModel
import com.kakao.sdk.user.UserApiClient

//class MainActivity2 : AppCompatActivity() {
//    lateinit var binding: ActivityMain2Binding
//    val model : UserViewModel by viewModels() //viewmodel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMain2Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        UserApiClient.instance.me { user, error ->
//            if (error != null) {
//                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
//            }
//            else if (user != null) {
//                Log.i(
//                    ContentValues.TAG, "사용자 정보 요청 성공" +
//                        "\n회원번호: ${user.id}" +
//                        "\n이메일: ${user.kakaoAccount?.email}" +
//                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
//                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
//                binding.txtEmail.text = user.kakaoAccount?.email
//
//                model.setUser(user.id.toString()!!,)
//            }
//        }
//
//        binding.btnKakaologout.setOnClickListener {
//            kakaologout()
//        }
//    }
//
//    private fun kakaologout() {
//        UserApiClient.instance.logout { error ->
//            if (error != null) {
//                Log.e(ContentValues.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
//            }
//            else {
//                Log.i(ContentValues.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                Toast.makeText(this,"카카오 로그아웃 성공", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}