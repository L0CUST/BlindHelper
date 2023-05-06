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
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blindhelper.adapter.RewardAdapter
import com.example.blindhelper.data.Reward
import com.example.blindhelper.databinding.FragmentRewardBinding
import com.example.blindhelper.databinding.RewardRecBinding
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import com.kakao.sdk.user.UserApiClient

class RewardFragment : Fragment() {
    lateinit var binding:FragmentRewardBinding
    private val model : UserViewModel by activityViewModels()
    private val obstacle : ObstacleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRewardBinding.inflate(inflater)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "로그인 안 함", error)
                Toast.makeText(context, "로그인을 한 사용자만 접근할 수 있습니다.",  Toast.LENGTH_SHORT).show()
                findNavController(this).navigate(R.id.action_rewardFragment_to_mapFragment)
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rewardArray = arrayOf(
            Reward("베스킨라빈스 파인트", "베스킨라빈스", "9000", 1),
            Reward("황금올리브 한마리 콤보", "BBQ", "20000", 2),
            Reward("아이스아메리카노", "스타벅스", "4000", 3),
            Reward("싸이버거 세트", "맘스터치", "6000", 4),
            Reward("츄파춥스250", "GS25", "250", 5),
            Reward("몬스터 화이트", "GS25", "1500", 6),
            Reward("포카칩" , "오리온", "2000", 7)
        )

        val adapter = RewardAdapter(rewardArray)
        binding.rewardRec.layoutManager = LinearLayoutManager(requireContext())
        binding.rewardRec.adapter = adapter
        val value = model.users.value
        binding.txtName.text = value?.name
        binding.txtEmail.text=value?.email
        binding.txtPointReward.text=value?.point.toString()
    }
}