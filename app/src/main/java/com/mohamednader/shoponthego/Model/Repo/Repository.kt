package com.mohamednader.shoponthego.Model.Repo

import android.util.Log
import com.mohamednader.shoponthego.Database.LocalSource
import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.CustomCollections.CustomCollection
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.SmartCollections.Brand
import com.mohamednader.shoponthego.Network.RemoteSource
import com.mohamednader.shoponthego.SharedPrefs.SharedPrefsSource
import kotlinx.coroutines.flow.Flow

class Repository constructor(
    remoteSource: RemoteSource, localSource: LocalSource, sharedPrefsSource: SharedPrefsSource
) : RepositoryInterface {

    private val TAG = "Repository_INFO_TAG"


    private val remoteSource: RemoteSource
    private val localSource: LocalSource
    private val sharedPrefsSource: SharedPrefsSource

    init {
        this.remoteSource = remoteSource
        this.localSource = localSource
        this.sharedPrefsSource = sharedPrefsSource
    }

    companion object {
        private var repo: Repository? = null
        fun getInstance(
            remoteSource: RemoteSource,
            localSource: LocalSource,
            sharedPrefsSource: SharedPrefsSource
        ): Repository {
            return repo ?: synchronized(this) {
                val instance = Repository(remoteSource, localSource, sharedPrefsSource)
                repo = instance
                instance
            }
        }
    }

    override suspend fun getAllProducts(): Flow<List<Product>> {
        Log.i(TAG, "getAllProducts: REPO")
        return remoteSource.getAllProducts()
    }

    override suspend fun getProductByID(productId: Long): Flow<Product> {
        return remoteSource.getProductByID(productId)
    }

    override suspend fun getProductsByCollectionID(collectionId: Long): Flow<List<Product>> {
        return remoteSource.getProductsByCollectionID(collectionId)
    }

    override suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>> {
        Log.i(TAG, "getDiscountCodesByPriceRuleID: REPO")
        return remoteSource.getDiscountCodesByPriceRuleID(priceRuleId)
    }

    override suspend fun getAllPriceRules(): Flow<List<PriceRules>> {
        Log.i(TAG, "getAllPriceRules: REPO")
        return remoteSource.getAllPriceRules()
    }


    override suspend fun getCurrencyConvertor(from: String, to: String): Flow<List<ToCurrency>> {
        return remoteSource.getCurrencyConvertor(from, to)
    }

    override suspend fun getAllCurrencies(): Flow<List<CurrencyInfo>> {
        return remoteSource.getAllCurrencies()
    }

    override suspend fun getAllBrands(): Flow<List<Brand>> {
        return remoteSource.getAllBrands()
    }

    override suspend fun getAllCollections(): Flow<List<CustomCollection>> {
        return remoteSource.getAllCollections()
    }

}