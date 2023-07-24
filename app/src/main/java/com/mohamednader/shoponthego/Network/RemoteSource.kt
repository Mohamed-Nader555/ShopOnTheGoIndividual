package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodes
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRules
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ToCurrency
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyInfo
import com.mohamednader.shoponthego.Model.Pojo.CustomCollections.CustomCollection
import com.mohamednader.shoponthego.Model.Pojo.Products.Product
import com.mohamednader.shoponthego.Model.Pojo.SmartCollections.Brand
import kotlinx.coroutines.flow.Flow

interface RemoteSource {

    //Products
    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductByID(productId: Long): Flow<Product>
    suspend fun getProductsByCollectionID(collectionId: Long): Flow<List<Product>>


    //Discount Codes
    suspend fun getDiscountCodesByPriceRuleID(priceRuleId: Long): Flow<List<DiscountCodes>>
    suspend fun getAllPriceRules(): Flow<List<PriceRules>>


    //Currency
    suspend fun getCurrencyConvertor(from: String, to: String): Flow<List<ToCurrency>>
    suspend fun getAllCurrencies(): Flow<List<CurrencyInfo>>

    //Brands
    suspend fun getAllBrands(): Flow<List<Brand>>

    //Collections
    suspend fun getAllCollections(): Flow<List<CustomCollection>>

}