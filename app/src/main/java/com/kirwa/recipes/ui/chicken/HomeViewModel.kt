package com.kirwa.recipes.ui.chicken

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirwa.recipes.GeneralResponse
import com.kirwa.recipes.network.RecipeApiClient
import com.kirwa.recipes.network.models.Recipe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {
    private val apiClient by lazy {
        RecipeApiClient.getClient()
    }
    private var disposable = CompositeDisposable()
    private val status = MutableLiveData<GeneralResponse>()
    var recipeCategory = MutableLiveData<List<Recipe>>()

    init {
        fetchRecipeCategory()
    }

    private fun fetchRecipeCategory(){
        disposable.add(apiClient.searchRecipe("chicken")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result ->
                    status.value = GeneralResponse.SUCCESS
                    recipeCategory.value = result.recipes
                },
                {
                    this::onError
                    status.value = GeneralResponse.ERROR
                }
            )
        )
    }

    private fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        Log.i("HomeViewModel", "HomeViewModel is cleared")
    }
}