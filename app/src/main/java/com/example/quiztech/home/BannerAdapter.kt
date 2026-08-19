package com.example.quiztech.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quiztech.R
import com.example.quiztech.databinding.ItemBannerBinding
import com.example.quiztech.model.BannerList

class BannerAdapter(private val banners: List<BannerList>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(banners[position])
    }

    override fun getItemCount(): Int = banners.size

    class BannerViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: BannerList) {
            Glide.with(binding.ivBanner.context)
                .load(banner.image)
                .placeholder(R.drawable.img_home_top_bg)
                .error(R.drawable.img_home_top_bg)
                .into(binding.ivBanner)
        }
    }
}