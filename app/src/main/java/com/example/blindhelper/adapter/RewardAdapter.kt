package com.example.blindhelper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blindhelper.R
import com.example.blindhelper.data.Reward
import com.example.blindhelper.databinding.RewardRecBinding

class RewardAdapter(val array:Array<Reward>): RecyclerView.Adapter<RewardAdapter.Holder>() {
    inner class Holder(val binding: RewardRecBinding) : RecyclerView.ViewHolder(binding.root) {
        fun dataBinding(reward:Reward){
            when (reward.id) {
                1->binding.imgGift.setImageResource(R.drawable.icecream)
                2->binding.imgGift.setImageResource(R.drawable.chicken)
                3->binding.imgGift.setImageResource(R.drawable.coffee)
                4->binding.imgGift.setImageResource(R.drawable.burger)
                5->binding.imgGift.setImageResource(R.drawable.candy)
            }
            binding.txtName.text=reward.name
            binding.txtStoreName.text=reward.store
            binding.txtPrice.text=reward.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardAdapter.Holder {
        val binding = RewardRecBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: RewardAdapter.Holder, position: Int) {
        holder.dataBinding(array[position])
    }

    override fun getItemCount(): Int {
        return array.size
    }
}