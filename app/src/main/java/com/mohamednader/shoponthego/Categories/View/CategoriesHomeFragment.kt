package com.mohamednader.shoponthego.Categories.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mohamednader.shoponthego.Categories.View.Adapters.ViewPager.CategoryViewPagerAdapter
import com.mohamednader.shoponthego.Categories.ViewModel.CategoriesViewModel
import com.mohamednader.shoponthego.Database.ConcreteLocalSource
import com.mohamednader.shoponthego.Model.Pojo.CustomCollections.CustomCollection
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.Repository
import com.mohamednader.shoponthego.Network.ApiClient
import com.mohamednader.shoponthego.Network.ApiState
import com.mohamednader.shoponthego.SharedPrefs.ConcreteSharedPrefsSource
import com.mohamednader.shoponthego.Utils.GenericViewModelFactory
import com.mohamednader.shoponthego.databinding.FragmentCategoriesHomeBinding
import kotlinx.coroutines.launch


class CategoriesHomeFragment : Fragment() {

    lateinit var adapter: CategoryViewPagerAdapter

    private val TAG = "CategoriesFragment_INFO_TAG"
    private lateinit var binding: FragmentCategoriesHomeBinding

    //View Model Members
    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var factory: GenericViewModelFactory

    //Needed Variables
    lateinit var categories: List<CustomCollection>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesHomeBinding.inflate(inflater, container, false)
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

        apiRequests()
    }

    private fun apiRequests() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            launch {
                categoriesViewModel.categoriesList.collect { result ->
                    when (result) {
                        is ApiState.Success<List<CustomCollection>> -> {
                            Log.i(TAG, "onCreate: Success...{${result.data.get(0).id}}")
                            categories = result.data
                            initIntent()
                        }
                        is ApiState.Loading -> {
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

        categoriesViewModel.getAllCategoriesFromNetwork()

    }

    private fun initIntent() {
        adapter = CategoryViewPagerAdapter(
            childFragmentManager, categories
        )
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.setCurrentItem(0, true)
        adapter.notifyDataSetChanged()
    }


}
