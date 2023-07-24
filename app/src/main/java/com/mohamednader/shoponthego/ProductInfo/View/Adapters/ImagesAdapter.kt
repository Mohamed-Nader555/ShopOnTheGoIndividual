package com.mohamednader.shoponthego.ProductInfo.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamednader.shoponthego.Model.Pojo.Products.Image
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ItemImageBinding

class ImageAdapter(private val context: Context, private val listener: OnImageClickListener) :
    ListAdapter<Image, ImageViewHolder>(ImageDiffUtil()) {


    private lateinit var binding: ItemImageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image: Image = getItem(position)

        Glide.with(context).load(image.src).placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.binding.image)


        holder.binding.root.setOnClickListener {
            listener.onImageClickListener()
        }

    }
}

class ImageViewHolder(var binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

class ImageDiffUtil : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }

}