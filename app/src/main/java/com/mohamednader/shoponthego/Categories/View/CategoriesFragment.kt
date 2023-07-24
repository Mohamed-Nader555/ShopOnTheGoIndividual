package com.mohamednader.shoponthego.Categories.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mohamednader.shoponthego.Categories.View.Adapters.Products.OnProductClickListener
import com.mohamednader.shoponthego.Categories.View.Adapters.Products.ProductAdapter
import com.mohamednader.shoponthego.Categories.ViewModel.CategoriesViewModel
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.ProductInfo.View.ProductInfoActivity
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.FragmentCategoriesBinding
import kotlinx.coroutines.launch

class CategoriesFragment : Fragment(), OnProductClickListener {


    private val TAG = "CategoriesFragment_INFO_TAG"
    private lateinit var binding: FragmentCategoriesBinding

    //View Model Members
    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var factory: GenericViewModelFactory

    //Adapter
    lateinit var productsAdapter: ProductAdapter

    //Layout Managers
    lateinit var productsLayoutManager: GridLayoutManager

    //Needed Variables
    val EXTRA_PRODUCT_ID = "productID"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() {

        factory = GenericViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                ConcreteSharedPrefsSource(requireContext())
            )
        )

        categoriesViewModel = ViewModelProvider(this, factory).get(CategoriesViewModel::class.java)


        productsAdapter = ProductAdapter(requireContext(), this)

        //Layouts
        productsLayoutManager = GridLayoutManager(requireContext(), 2)

        binding.categoriesRecyclerView.apply {
            adapter = productsAdapter
            layoutManager = productsLayoutManager
        }

        apiRequests()

    }

    private fun apiRequests() {
        val categoryID = requireArguments().getLong("EXTRA_DATA_ID", 0)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            launch {
                categoriesViewModel.productsList.collect { result ->
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
                                requireContext(), "There Was An Error", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        categoriesViewModel.getCategoryProductsFromNetwork(categoryID)

    }

    override fun onProductClickListener(id: Long) {
         val intent: Intent = Intent(requireContext() ,ProductInfoActivity::class.java )
        intent.putExtra(EXTRA_PRODUCT_ID, id)
        startActivity(intent)
    }


}