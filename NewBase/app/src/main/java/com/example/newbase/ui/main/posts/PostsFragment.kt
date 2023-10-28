package com.example.newbase.ui.main.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newbase.R
import com.example.newbase.core.entity.Resource
import com.example.newbase.data.entities.PostDetail
import com.example.newbase.databinding.FragmentPostsBinding
import com.example.newbase.ui.main.adapter.PostAdapter
import com.example.newbase.util.NetworkStateDelegate
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostsFragment : Fragment(), PostAdapter.itemClickListener {

    @Inject
    lateinit var networkStateDelegate: NetworkStateDelegate

    private val viewModel: PostViewModel by activityViewModels()
    private var binding: FragmentPostsBinding? = null
    private val pokemonAdapter: PostAdapter by lazy {
        PostAdapter(requireContext(), arrayListOf()).apply {
            setItemListener(this@PostsFragment)
        }
    }

    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        snackbar = Snackbar.make(view, "No hay conexión a internet", Snackbar.LENGTH_INDEFINITE)
        snackbar?.setAction("OK"){
            snackbar?.dismiss()
        }
        networkStateDelegate.networkState.observe(viewLifecycleOwner, Observer { isConnected ->
            if (isConnected) {
                snackbar?.dismiss()
            } else {
                snackbar?.show()
            }
        })
        viewModel.getPosts()
        initObservers()

    }


    private fun setUpView() {
        binding?.pokeRv?.let {
            it.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = pokemonAdapter
            }
        }

        binding?.searchEditText?.addTextChangedListener{ editable ->
            val query = editable?.toString() ?: ""
            pokemonAdapter.filter.filter(query)
        }

        binding?.fabAdd?.setOnClickListener {
            viewModel.selectedPost = null
            viewModel.navController?.navigate(R.id.action_pokeFragment_to_pokeDetailFragment)
        }
    }

    private fun initObservers() {
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            if(it!=null){
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding?.progressBar?.visibility = View.GONE
                    if(!it.data.isNullOrEmpty()) pokemonAdapter.addItems(it.data)
                }
                Resource.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING ->
                    binding?.progressBar?.visibility = View.VISIBLE
            }
        }}
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClicked(poke: PostDetail) {
        viewModel.selectedPost = poke
        viewModel.navController?.navigate(R.id.action_pokeFragment_to_pokeDetailFragment)
    }

    override fun onPause() {
        super.onPause()
    }

}