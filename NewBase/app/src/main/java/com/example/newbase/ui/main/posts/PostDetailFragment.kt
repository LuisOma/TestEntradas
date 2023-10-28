package com.example.newbase.ui.main.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.newbase.data.entities.PostDetail
import com.example.newbase.databinding.FragmentPostDetailBinding
import com.example.newbase.util.NetworkStateDelegate
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    @Inject
    lateinit var networkStateDelegate: NetworkStateDelegate

    private val viewModel: PostViewModel by activityViewModels()
    private var binding: FragmentPostDetailBinding? = null
    private var snackbar: Snackbar? = null
    private var buttonEnabled = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
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
                buttonEnabled = true
                enableButton(buttonEnabled)
                snackbar?.dismiss()
            } else {
                buttonEnabled = false
                enableButton(buttonEnabled)
                snackbar?.show()
            }
        })

        viewModel.savePostStatus.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                viewModel.getPosts()
                activity?.onBackPressed()
                viewModel.savePostStatus.postValue(null)
            }
        })
    }

    private fun setUpView() {

        viewModel.selectedPost?.let{
            binding?.edtTitle?.apply {
                setText(it?.title)
                isEnabled = false
            }
            binding?.edtAuthor?.apply {
                setText(it?.author)
                isEnabled = false

            }
            binding?.edtDate?.apply {
                setText(it?.date)
                isEnabled = false

            }
            binding?.edtContent?.apply {
                setText(it?.content)
                isEnabled = false

            }
        }
        if (viewModel.selectedPost == null){
            binding?.btnSave?.visibility = View.VISIBLE
        }

        binding?.btnSave?.setOnClickListener {
            if(buttonEnabled){
                if(validateData()){
                    viewModel.savePost(PostDetail(
                        title = binding?.edtTitle?.text?.toString(),
                        author = binding?.edtAuthor?.text?.toString(),
                        date = binding?.edtDate?.text?.toString(),
                        content = binding?.edtContent?.text?.toString(),
                    ))
                }
                else{
                    Toast.makeText(context, "Ninguno de los datos debe estar vacío",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                snackbar?.show()
            }
        }

    }

    fun enableButton(enabled: Boolean){
        binding?.btnSave?.isEnabled = enabled
    }

    fun validateData(): Boolean{
        return !binding?.edtTitle?.text.isNullOrEmpty() &&
                !binding?.edtAuthor?.text.isNullOrEmpty() &&
                !binding?.edtDate?.text.isNullOrEmpty() &&
                !binding?.edtContent?.text.isNullOrEmpty()
    }

}