package com.mohamednader.shoponthego.Home.View.Adapters.Brands

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamednader.shoponthego.Model.Pojo.SmartCollections.Brand
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ItemBrandBinding

class BrandAdapter(private val context: Context, private val listener: OnBrandClickListener) :
    ListAdapter<Brand, BrandViewHolder>(BrandDiffUtil()) {


    private lateinit var binding: ItemBrandBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        binding = ItemBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brand: Brand = getItem(position)

        holder.binding.brandName.text = brand.title

        Glide.with(context).load(brand.image.src).placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.binding.brandImage)


        holder.binding.root.setOnClickListener {
            listener.onBrandClickListener(brand.id)
        }
    }
}

class BrandViewHolder(var binding: ItemBrandBinding) : RecyclerView.ViewHolder(binding.root)

class BrandDiffUtil : DiffUtil.ItemCallback<Brand>() {
    override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem == newItem
    }

}