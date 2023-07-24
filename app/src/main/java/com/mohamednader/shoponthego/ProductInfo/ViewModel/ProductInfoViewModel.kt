package com.mohamednader.shoponthego.ProductInfo.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Repo.RepositoryInterface
import com.mohamednader.shoponthego.Network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductInfoViewModel (private val repo: RepositoryInterface) : ViewModel() {

    private val TAG = "ProductInfoViewModel_INFO_TAG"

    private var _product: MutableStateFlow<ApiState<Product>> =
        MutableStateFlow<ApiState<Product>>(ApiState.Loading)
    val product: StateFlow<ApiState<Product>>
        get() = _product

    fun getProductByIdFromNetwork(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "getProductByIdFromNetwork:  ViewModel")
            repo.getProductByID(productId)
                .catch { e -> _product.value = ApiState.Failure(e) }
                .collect { data ->
                    _product.value = ApiState.Success(data)
                }
        }
    }
}