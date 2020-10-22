package com.kirwa.recipes.ui.dinner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kirwa.recipes.ViewModelFactory
import com.kirwa.recipes.adapter.RecipeAdapter
import com.kirwa.recipes.databinding.FragmentBreakfastBinding
import com.kirwa.recipes.databinding.FragmentDinnerBinding
import com.google.android.material.snackbar.Snackbar

class DinnerFragment : Fragment() {

    lateinit var binding: FragmentDinnerBinding
    private lateinit var viewModel: DinnerViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDinnerBinding.inflate(inflater)
        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(DinnerViewModel::class.java)

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