package com.mohamednader.shoponthego.BrandProducts.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mohamednader.shoponthego.BrandProducts.ViewModel.BrandProductsViewModel
import com.mohamednader.shoponthego.Categories.View.Adapters.Products.OnProductClickListener
import com.mohamednader.shoponthego.Categories.View.Adapters.Products.ProductAdapter
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.ProductInfo.View.ProductInfoActivity
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.ActivityBrandProductsBinding
import kotlinx.coroutines.launch

class BrandProductsActivity : AppCompatActivity(), OnProductClickListener {

    private val TAG = "BrandProductsActivity_INFO_TAG"
    private lateinit var binding: ActivityBrandProductsBinding

    //View Model Members
    private lateinit var brandProductsViewModel: BrandProductsViewModel
    private lateinit var factory: GenericViewModelFactory

    //Adapter
    lateinit var productsAdapter: ProductAdapter

    //Layout Managers
    lateinit var productsLayoutManager: GridLayoutManager

    //Needed Variables
    val EXTRA_CATEGORY_ID = "categoryID"
    var categoryID: Long = 0
    val EXTRA_PRODUCT_ID = "productID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initViews()

    }

    private fun initViews() {

        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(this@BrandProductsActivity),
                ConcreteSharedPrefsSource(this@BrandProductsActivity)
            )
        )

        brandProductsViewModel =
            ViewModelProvider(this, factory).get(BrandProductsViewModel::class.java)



        productsAdapter = ProductAdapter(this@BrandProductsActivity, this)

        //Layouts
        productsLayoutManager = GridLayoutManager(this@BrandProductsActivity, 2)

        binding.productsRecyclerView.apply {
            adapter = productsAdapter
            layoutManager = productsLayoutManager
        }

        apiRequests()

    }

    private fun apiRequests() {
        val categoryID = intent.getLongExtra(EXTRA_CATEGORY_ID, 0)
        lifecycleScope.launchWhenStarted {
            launch {
                brandProductsViewModel.productsList.collect { result ->
                    when (result) {
                        is ApiState.Success<List<Product>> -> {
                            Log.i(TAG, "onCreate: Success...{${result.data.get(0).id}}")
                            productsAdapter.submitList(result.data)
                            binding.progressBar.visibility = View.GONE
                        }
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            Log.i(TAG, "onCreate: Loading...")
                        }
                        is ApiState.Failure -> {
                            Toast.makeText(
                                this@BrandProductsActivity, "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        brandProductsViewModel.getCategoryProductsFromNetwork(categoryID)

    }


    override fun onProductClickListener(id: Long) {
         val intent: Intent = Intent(this@BrandProductsActivity, ProductInfoActivity::class.java)
        intent.putExtra(EXTRA_PRODUCT_ID, id)
        startActivity(intent)
    }

}