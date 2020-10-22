package com.kirwa.recipes.network.models


import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @SerializedName("recipe")
    val recipe: RecipeData
)