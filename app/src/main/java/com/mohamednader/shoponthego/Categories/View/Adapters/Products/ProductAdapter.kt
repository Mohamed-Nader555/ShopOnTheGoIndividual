package com.mohamednader.shoponthego.Categories.View.Adapters.Products

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.R
import com.mohamednader.shoponthego.databinding.ItemProductBinding

class ProductAdapter(private val context: Context, private val listener: OnProductClickListener) :
    ListAdapter<Product, ProductViewHolder>(ProductDiffUtil()) {


    private lateinit var binding: ItemProductBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product: Product = getItem(position)

        holder.binding.productName.text = product.vendor
        holder.binding.productDesc.text = product.variants?.get(0)?.title ?: ""
        holder.binding.productName.text = product.vendor

        Glide.with(context).load(product.image.src).placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.binding.brandImage)


        holder.binding.root.setOnClickListener {
            listener.onProductClickListener(product.id)
        }
    }
}

class ProductViewHolder(var binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

class ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

}
