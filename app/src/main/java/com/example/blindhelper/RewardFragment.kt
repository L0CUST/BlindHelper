package com.example.blindhelper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blindhelper.adapter.RewardAdapter
import com.example.blindhelper.data.Reward
import com.example.blindhelper.databinding.FragmentRewardBinding
import com.example.blindhelper.databinding.RewardRecBinding

class RewardFragment : Fragment() {
    lateinit var binding:FragmentRewardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= FragmentRewardBinding.inflate(inflater)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rewardArray = arrayOf(
            Reward("베스킨라빈스 파인트", "베스킨라빈스", "9000"),
            Reward("황금올리브 한마리 콤보", "BBQ", "20000"),
            Reward("아이스아메리카노", "스타벅스", "4000"),
            Reward("싸이버거 세트", "맘스터치", "6000"),
            Reward("츄파춥스250", "GS25", "250")
        )

        val adapter = RewardAdapter(rewardArray)
        binding.rewardRec.layoutManager = LinearLayoutManager(requireContext())
        binding.rewardRec.adapter = adapter
    }


}