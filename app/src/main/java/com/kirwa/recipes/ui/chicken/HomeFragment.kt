package com.kirwa.recipes.ui.chicken

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
import com.kirwa.recipes.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    private lateinit var viewModel: HomeViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

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