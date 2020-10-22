package com.kirwa.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kirwa.recipes.ui.breakfast.BreakfastViewModel
import com.kirwa.recipes.ui.chicken.HomeViewModel
import com.kirwa.recipes.ui.brunch.BrunchViewModel
import com.kirwa.recipes.ui.dinner.DinnerViewModel


class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel() as T
        }
        if (modelClass.isAssignableFrom(BreakfastViewModel::class.java)){
            return BreakfastViewModel() as T
        }
        if (modelClass.isAssignableFrom(BrunchViewModel::class.java)){
            return BrunchViewModel() as T
        }
        if (modelClass.isAssignableFrom(DinnerViewModel::class.java)){
            return DinnerViewModel() as T
        }
        throw IllegalArgumentException("Unknown viewModel")
    }
}
