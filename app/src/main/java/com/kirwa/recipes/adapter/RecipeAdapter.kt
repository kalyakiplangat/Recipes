package com.kirwa.recipes.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kirwa.recipes.R
import com.kirwa.recipes.databinding.CategoryListItemBinding
import com.kirwa.recipes.network.models.Recipe
import com.kirwa.recipes.ui.detail.DetailActivity
import kotlin.math.roundToInt

class RecipeAdapter(private var rootView: View) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    var recipeList = mutableListOf<Recipe>()

    inner class RecipeViewHolder(binding: CategoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var imageView = binding.recipeImage
        val title = binding.recipeTitle
        val ratings = binding.recipeSocialScore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(rootView.context)
        val binding = CategoryListItemBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun getItemCount() = recipeList.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        Glide.with(rootView.context)
            .load(recipe.imageUrl)
            .apply(
                RequestOptions().placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(holder.imageView)
        holder.title.text = recipe.title.toString()
        holder.ratings.text = recipe.socialRank.roundToInt().toString()

        holder.itemView.setOnClickListener {
            val recipe = recipeList[position]
            val intent = Intent(rootView.context, DetailActivity::class.java)
            intent.putExtra("rId", recipe.recipeId)
            rootView.context.startActivity(intent)
        }

    }

    fun setItems(list: List<Recipe>) {
        recipeList.clear()
        list.forEach {
            recipeList.add(it)
        }
        notifyDataSetChanged()
    }

}