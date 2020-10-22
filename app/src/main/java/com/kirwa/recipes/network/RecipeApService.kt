package com.kirwa.recipes.network

import com.kirwa.recipes.network.models.DetailResponse
import com.kirwa.recipes.network.models.RecipesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Author by Cheruiyot Enock on 10/12/20.
 */
interface RecipeApService {

    @GET("api/search")
    fun getCategory(): Observable<RecipesResponse>

    @GET("api/search")
    fun searchRecipe(
        @Query("q") query: String
    ): Observable<RecipesResponse>

    @GET("api/get")
    fun getRecipeDetail(
        @Query("rId") query: String
    ): Observable<DetailResponse>

}