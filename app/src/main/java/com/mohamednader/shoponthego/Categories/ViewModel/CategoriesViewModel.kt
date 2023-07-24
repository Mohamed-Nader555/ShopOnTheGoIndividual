package com.mohamednader.shoponthego.Categories.ViewModel

import android.util.Log
import androidx.annotation.LongDef
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.CustomCollections.CustomCollection
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "CategoriesViewModel_INFO_TAG"

    private var _categoriesList: MutableStateFlow<ApiState<List<CustomCollection>>> =
        MutableStateFlow<ApiState<List<CustomCollection>>>(ApiState.Loading)
    val categoriesList: StateFlow<ApiState<List<CustomCollection>>>
        get() = _categoriesList


    private var _productsList: MutableStateFlow<ApiState<List<Product>>> =
        MutableStateFlow<ApiState<List<Product>>>(ApiState.Loading)
    val productsList: StateFlow<ApiState<List<Product>>>
        get() = _productsList


    //Categories
    fun getAllCategoriesFromNetwork() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getAllCategoriesFromNetwork:  ViewModel")
            repo.getAllCollections()
                .catch { e -> _categoriesList.value = ApiState.Failure(e) }
                .collect { data ->
                    _categoriesList.value = ApiState.Success(data)
                }
        }
    }

    //Products
    fun getCategoryProductsFromNetwork(collectionID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getCategoryProductsFromNetwork:  CategoriesViewModel")
            repo.getProductsByCollectionID(collectionID)
                .catch { e -> _productsList.value = ApiState.Failure(e) }
                .collect { data ->
                    _productsList.value = ApiState.Success(data)
                }
        }
    }


}