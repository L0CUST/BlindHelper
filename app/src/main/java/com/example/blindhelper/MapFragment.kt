package com.example.blindhelper

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.blindhelper.data.Obstacle
import com.example.blindhelper.data.User
import com.example.blindhelper.databinding.FragmentMapBinding
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient


class MapFragment : Fragment() {
    private val model : UserViewModel by activityViewModels()
    private val obstacle : ObstacleViewModel by activityViewModels()
    private lateinit var binding:FragmentMapBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var mDbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val obsArr=obstacle.obstacles.value

        super.onViewCreated(view, savedInstanceState)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error == null) {
                binding.btnKakaoLogin.visibility=View.INVISIBLE
            }else{
                binding.btnKakaoLogin.visibility=View.VISIBLE
            }


        }
        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin()
        }

    }
    private fun kakaoLogin() {

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                getUserInfo()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    getUserInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }

    private fun getUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                val kname = "${user.kakaoAccount?.profile?.nickname}"
                val kemail = "${user.kakaoAccount?.email}"
                val kid = "${user.id}"

                signUp(kname, kemail, kid)
            }
        }
    }

    private fun signUp(name: String, email: String, uid: String) {
        mAuth.createUserWithEmailAndPassword(email, uid)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    addUserToDb(name, email, uid)
                }
                else {
//                    val intent: Intent = Intent(this, MainActivity2::class.java)
//                    startActivity(intent)
                    Toast.makeText(requireContext(),"카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDb(name: String, email: String, uId: String) {
        mDbRef.child("user").child(uId).setValue(User(name, email, uId, 0))
//        val intent: Intent = Intent(this, MainActivity2::class.java)
//        startActivity(intent)
        Toast.makeText(requireContext(),"카카오 로그인 성공", Toast.LENGTH_SHORT).show()
    }

}