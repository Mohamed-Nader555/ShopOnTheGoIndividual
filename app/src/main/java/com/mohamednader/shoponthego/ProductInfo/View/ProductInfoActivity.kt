package com.mohamednader.shoponthego.ProductInfo.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mohamednader.shoponthego.Cart.View.CartActivity
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.ProductInfo.View.Adapters.ImageAdapter
import com.mohamednader.shoponthego.ProductInfo.View.Adapters.OnImageClickListener
import com.mohamednader.shoponthego.ProductInfo.ViewModel.ProductInfoViewModel
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityProductInfoBinding
import kotlinx.coroutines.launch

class ProductInfoActivity : AppCompatActivity(), OnImageClickListener {

    val TAG = "ProductInfoActivity_INFO_TAG"

    private lateinit var binding: ActivityProductInfoBinding

    //View Model Members
    private lateinit var productInfoViewModel: ProductInfoViewModel
    private lateinit var factory: GenericViewModelFactory

    //Adapters
    lateinit var imageAdapter: ImageAdapter

    //Needed Variables
    val EXTRA_PRODUCT_ID = "productID"
    var productID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productID = intent.getLongExtra(EXTRA_PRODUCT_ID, 0)

        initViews()

    }

    private fun initViews() {
        //View Model
        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(this@ProductInfoActivity),
                ConcreteSharedPrefsSource(this@ProductInfoActivity)
            )
        )
        productInfoViewModel =
            ViewModelProvider(this, factory).get(ProductInfoViewModel::class.java)

        imageAdapter = ImageAdapter(this@ProductInfoActivity, this)

        binding.imagesViewPager.apply {
            adapter = imageAdapter
        }

        binding.backImg.setOnClickListener{
            onBackPressed()
        }

        binding.cartImg.setOnClickListener{
            val intent = Intent(this@ProductInfoActivity, CartActivity::class.java)
            startActivity(intent)
        }

        apiRequest()
    }

    private fun apiRequest() {
        lifecycleScope.launchWhenStarted {
            launch {
                productInfoViewModel.product.collect { result ->
                    when (result) {
                        is ApiState.Success<Product> -> {
                            val product = result.data
                            Log.i(TAG, "onCreate: Success...{${result.data.title}}")
                            Log.i(TAG, "onCurrencyClickListener: you Clicked ${result.data.title}")
                            imageAdapter.submitList(product.images)
                            binding.indicator.setViewPager(binding.imagesViewPager)
                            binding.productTitle.text = product.title
                            binding.productVendor.text = product.variants!!.get(0).title
                            binding.ratingBar.rating = 4.5F
                            binding.productDesc.text = product.bodyHtml
                            binding.productPrice.text = "${product.variants!!.get(0).price} EGP"
                            binding.availableText.text = "Availabe In Stock (${product.variants!!.get(0).inventoryQuantity})"

                        }
                        is ApiState.Loading -> {
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            //hideViews()
                            Toast.makeText(
                                this@ProductInfoActivity, "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        productInfoViewModel.getProductByIdFromNetwork(productID)
    }

    override fun onImageClickListener() {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}