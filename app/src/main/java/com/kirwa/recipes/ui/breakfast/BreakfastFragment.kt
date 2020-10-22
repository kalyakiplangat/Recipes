package com.kirwa.recipes.ui.breakfast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kirwa.recipes.ViewModelFactory
import com.kirwa.recipes.adapter.RecipeAdapter
import com.kirwa.recipes.databinding.FragmentBreakfastBinding
import com.google.android.material.snackbar.Snackbar

class BreakfastFragment : Fragment() {
    lateinit var binding: FragmentBreakfastBinding
    private lateinit var viewModel: BreakfastViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakfastBinding.inflate(inflater)
        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(BreakfastViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        recyclerView = binding.recycler
        recipeAdapter = RecipeAdapter(binding.root)
        recyclerView.adapter = recipeAdapter

        viewModel.recipeCategory.observe(viewLifecycleOwner, Observer {
            recipeAdapter.setItems(it)
            Snackbar.make(binding.root,"Found ${it.size} recipes", Snackbar.LENGTH_SHORT ).show()
        })

        return binding.root
    }
}