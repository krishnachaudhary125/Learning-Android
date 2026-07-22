package com.example.atry.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.atry.databinding.ItemOptionBtnBinding

class OptionAdapter : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    private val options = mutableListOf<String>()

    fun submitList(list: List<String>) {
        options.clear()
        options.addAll(list)
        notifyDataSetChanged()
    }

    inner class OptionViewHolder(val binding: ItemOptionBtnBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemOptionBtnBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.binding.rbOption.text = options[position]
    }

    override fun getItemCount() = options.size
}