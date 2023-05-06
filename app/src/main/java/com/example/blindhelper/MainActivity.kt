package com.example.blindhelper

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.blindhelper.data.Reward
import com.example.blindhelper.data.User
import com.example.blindhelper.databinding.ActivityMainBinding
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var mDbRef: DatabaseReference
    private val userModel : UserViewModel by viewModels()
    private val obstacleModel : ObstacleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KakaoSdk.init(this, "bbd52a5712e9ace62e2e65bbe667f069")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        mDbRef = Firebase.database.reference

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "토큰 정보 보기 실패", error)
            }
            else if (tokenInfo != null) {
                Log.i(
                    ContentValues.TAG, "토큰 정보 보기 성공" +
                        "\n회원번호: ${tokenInfo.id}" +
                        "\n만료시간: ${tokenInfo.expiresIn} 초")
//                val intent = Intent(this, MainActivity2::class.java)
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
                    }
                    else if (user != null) {
                        Log.i(
                            ContentValues.TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                        val user1 = User(
                            user.kakaoAccount?.profile?.nickname!!,
                            user.kakaoAccount?.email!!,
                            user.id.toString(),
                            0
                        )
                        userModel.setUser(user1)
                    }
//                startActivity(intent)
                Toast.makeText(this,"자동 로그인 성공", Toast.LENGTH_SHORT).show()
            }
        }}
        binding.container.setOnClickListener{
            val intent = Intent(this, ObstacleActivity::class.java)
            intent.putExtra("uid", userModel.users.value?.uId!!)
            startActivity(intent)

        }
        binding.bottom.setupWithNavController(binding.container.getFragment<NavHostFragment>().navController)



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

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    getUserInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
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

                signUp(kname, kemail, kid, 0)
            }
        }
    }

    private fun signUp(name: String, email: String, uid: String, point:Int) {
        mAuth.createUserWithEmailAndPassword(email, uid)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDb(name, email, uid, point)
                }
                else {
//                    val intent: Intent = Intent(this, MainActivity2::class.java)
//                    startActivity(intent)
                    Toast.makeText(this,"카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDb(name: String, email: String, uId: String, point:Int) {
        mDbRef.child("user").child(uId).setValue(User(name, email, uId, point))
//        val intent: Intent = Intent(this, MainActivity2::class.java)
//        startActivity(intent)
        Toast.makeText(this,"카카오 로그인 성공", Toast.LENGTH_SHORT).show()
    }

}