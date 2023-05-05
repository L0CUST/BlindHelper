package com.example.blindhelper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blindhelper.data.Reward
import com.example.blindhelper.databinding.RewardRecBinding

class RewardAdapter(val array:Array<Reward>): RecyclerView.Adapter<RewardAdapter.Holder>() {
    inner class Holder(val binding: RewardRecBinding) : RecyclerView.ViewHolder(binding.root) {
        fun dataBinding(reward:Reward){
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