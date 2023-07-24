package com.mohamednader.shoponthego.Network

import android.util.Log
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.CustomCollections.CustomCollection
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.SmartCollections.Brand
import com.mohamednader.shoponthego.Utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import okhttp3.Credentials

class ApiClient : RemoteSource {


    private val TAG = "ApiClient_INFO_TAG"


    val apiService: ApiService by lazy {
        RetrofitHelper.getInstance(Constants.shopifyBaseUrl).create(ApiService::class.java)
    }

    val apiServiceForCurrency: ApiService by lazy {
        RetrofitHelper.getInstance(Constants.currencyBaseUrl).create(ApiService::class.java)
    }

    companion object {
        private var instance: ApiClient? = null
        fun getInstance(): ApiClient {
            return instance ?: synchronized(this) {
                val temp = ApiClient()
                instance = temp
                temp
            }
        }
    }


    //Products
    override suspend fun getAllProducts(): Flow<List<Product>> {
        val response = apiService.getAllProducts()
        val productsList: Flow<List<Product>>
        Log.i(TAG, "getAllProducts: API-Client")
        if (response.isSuccessful) {
            productsList = flowOf(response.body()!!.products)
            Log.i(TAG, "getAllProducts: Done")
        } else {
            productsList = emptyFlow()
            Log.i(TAG, "getAllProducts: ${response.errorBody().toString()}")
        }
        return productsList
    }


    override suspend fun getProductByID(productId: Long): Flow<Product> {
        val response = apiService.getProductByID(productId)
        val product: Flow<Product>
        Log.i(TAG, "getProductByID: API-Client")
        if (response.isSuccessful) {
            product = flowOf(response.body()!!.product)
            Log.i(TAG, "getProductByID: Done")
        } else {
            product = emptyFlow()
            Log.i(TAG, "getProductByID: ${response.errorBody().toString()}")
        }
        return product
    }

    override suspend fun getProductsByCollectionID(collectionId: Long): Flow<List<Product>> {
        val response = apiService.getProductsByCollectionID(collectionId)
        val productsList: Flow<List<Product>>
        Log.i(TAG, "getProductsByCollectionID: API-Client")
        if (response.isSuccessful) {
            productsList = flowOf(response.body()!!.products)
            Log.i(TAG, "getProductsByCollectionID: Done")
        } else {
            productsList = emptyFlow()
            Log.i(TAG, "getProductsByCollectionID: ${response.errorBody().toString()}")
        }
        return productsList
    }



    //Currencies
    override suspend fun getCurrencyConvertor(from: String, to: String): Flow<List<ToCurrency>> {
        val response = apiServiceForCurrency.getCurrencyConvertor(
            Credentials.basic(
                Constants.currencyApiUsername, Constants.currencyApiPassword
            ), from, to
        )
        val currencyRes: Flow<List<ToCurrency>>
        Log.i(TAG, "getCurrencyConvertor API-Client")
        if (response.isSuccessful) {
            currencyRes = flowOf(response.body()!!.to)
            Log.i(TAG, "getCurrencyConvertor: Done")

        } else {
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "getCurrencyConvertor: Error: $errorBody")
            currencyRes = emptyFlow()
        }
        return currencyRes
    }

    override suspend fun getAllCurrencies(): Flow<List<CurrencyInfo>> {
        val response = apiServiceForCurrency.getAllCurrencies(
            Credentials.basic(
                Constants.currencyApiUsername, Constants.currencyApiPassword
            )
        )
        val currencyRes: Flow<List<CurrencyInfo>>
        Log.i(TAG, "getAllCurrencies API-Client")
        if (response.isSuccessful) {
            currencyRes = flowOf(response.body()!!.currencies)
            Log.i(TAG, "getAllCurrencies: Done")

        } else {
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "getAllCurrencies: Error: $errorBody")
            currencyRes = emptyFlow()
        }
        return currencyRes
    }


    //Discount Codes
    override suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>> {
        val response = apiService.getDiscountCodesByPriceRuleID(priceRuleId)
        val discountCodesList: Flow<List<DiscountCodes>>
        Log.i(TAG, "getDiscountCodesByPriceRuleID: API-Client")
        if (response.isSuccessful) {
            discountCodesList = flowOf(response.body()!!.discountCodesList)
            Log.i(TAG, "getDiscountCodesByPriceRuleID: Done")
        } else {
            discountCodesList = emptyFlow()
            Log.i(TAG, "getDiscountCodesByPriceRuleID: ${response.errorBody().toString()}")
        }
        return discountCodesList
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRules>> {
        val response = apiService.getAllPriceRules()
        val priceRulesList: Flow<List<PriceRules>>
        Log.i(TAG, "getAllPriceRules: API-Client")
        if (response.isSuccessful) {
            priceRulesList = flowOf(response.body()!!.priceRulesList)
            Log.i(TAG, "getAllPriceRules: Done")
        } else {
            priceRulesList = emptyFlow()
            Log.i(TAG, "getAllPriceRules: ${response.errorBody().toString()}")
        }
        return priceRulesList
    }


    //Brands
    override suspend fun getAllBrands(): Flow<List<Brand>> {
        val response = apiService.getAllBrands()
        val brandsList: Flow<List<Brand>>
        Log.i(TAG, "getAllBrands: API-Client")
        if (response.isSuccessful) {
            brandsList = flowOf(response.body()!!.smart_collections)
            Log.i(TAG, "getAllBrands: Done")
        } else {
            brandsList = emptyFlow()
            Log.i(TAG, "getAllBrands: ${response.errorBody().toString()}")
        }
        return brandsList
    }


    //Collections
    override suspend fun getAllCollections(): Flow<List<CustomCollection>> {
        val response = apiService.getAllCollections()
        val collectionList: Flow<List<CustomCollection>>
        Log.i(TAG, "getAllCollections: API-Client")
        if (response.isSuccessful) {
            collectionList = flowOf(response.body()!!.custom_collections)
            Log.i(TAG, "getAllCollections: Done")
        } else {
            collectionList = emptyFlow()
            Log.i(TAG, "getAllCollections: ${response.errorBody().toString()}")
        }
        return collectionList
    }


}