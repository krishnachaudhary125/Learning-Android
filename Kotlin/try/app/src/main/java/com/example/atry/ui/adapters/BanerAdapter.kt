package com.example.atry.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.atry.R
import com.example.atry.data.models.Banner
import com.example.atry.databinding.ItemBannerBinding

class BannerAdapter(
    private val onBannerClick: (Banner) -> Unit
) : ListAdapter<Banner, BannerAdapter.BannerViewHolder>(BannerDiffCallback()) {

    inner class BannerViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(banner: Banner) {
            Glide.with(binding.root.context)
                .load(banner.imageUrl)
                .placeholder(R.drawable.banner)
                .into(binding.bannerImage)

            binding.root.setOnClickListener {
                onBannerClick(banner)
            }

            binding.shopNowBtn.setOnClickListener {
                onBannerClick(banner)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BannerDiffCallback : DiffUtil.ItemCallback<Banner>() {
        override fun areItemsTheSame(oldItem: Banner, newItem: Banner) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Banner, newItem: Banner) =
            oldItem == newItem
    }
}