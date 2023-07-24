package com.mohamednader.shoponthego.Network

import com.mohamednader.shoponthego.Model.Pojo.Coupon.DiscountCodes.DiscountCodesResponse
import com.mohamednader.shoponthego.Model.Pojo.Coupon.PriceRules.PriceRulesResponse
import com.mohamednader.shoponthego.Model.Pojo.Currency.ConvertCurrency.ConvertCurrencyResponse
import com.mohamednader.shoponthego.Model.Pojo.Currency.Currencies.CurrencyResponse
import com.mohamednader.shoponthego.Model.Pojo.CustomCollections.CustomCollectionResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.ProductResponse
import com.mohamednader.shoponthego.Model.Pojo.Products.SingleProductResponse
import com.mohamednader.shoponthego.Model.Pojo.SmartCollections.BrandResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    //Products
    @Headers("X-Shopify-Access-Token: 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")
    @GET("products.json")
    suspend fun getAllProducts(): Response<ProductResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products/{id}.json")
    suspend fun getProductByID(@Path("id") productId: Long): Response<SingleProductResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("products.json")
    suspend fun getProductsByCollectionID(@Query("collection_id") collectionId: Long): Response<ProductResponse>


    //Discount Codes
    @Headers("X-Shopify-Access-Token: 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")
    @GET("price_rules/{priceRuleId}/discount_codes.json")
    suspend fun getDiscountCodesByPriceRuleID(@Path("priceRuleId") priceRuleId: Long): Response<DiscountCodesResponse>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("price_rules.json")
    suspend fun getAllPriceRules(): Response<PriceRulesResponse>


    //Currency
    @GET("convert_from.json")
    suspend fun getCurrencyConvertor(
        @Header("Authorization") authHeader: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): Response<ConvertCurrencyResponse>

    @GET("currencies.json")
    suspend fun getAllCurrencies(@Header("Authorization") authHeader: String): Response<CurrencyResponse>

    //Brands
    @Headers("X-Shopify-Access-Token: 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")
    @GET("smart_collections.json")
    suspend fun getAllBrands(): Response<BrandResponse>


    //Collections
    @Headers("X-Shopify-Access-Token: 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")
    @GET("custom_collections.json")
    suspend fun getAllCollections(): Response<CustomCollectionResponse>


}