package com.kirwa.recipes.network.models


import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("recipes")
    val recipes: List<Recipe>
)