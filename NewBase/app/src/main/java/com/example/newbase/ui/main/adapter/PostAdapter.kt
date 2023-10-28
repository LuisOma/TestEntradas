package com.example.newbase.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.newbase.constants.Constants
import com.example.newbase.core.entity.Resource
import com.example.newbase.data.entities.PostDetail
import com.example.newbase.databinding.NetworkStateItemBinding
import com.example.newbase.databinding.PostItemBinding
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter(private val context: Context, private val postList: ArrayList<PostDetail>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {
    private var networkState: Resource.Status? = null
    private var listener: itemClickListener? = null

    var postFilterList = ArrayList<PostDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.POST_VIEW_TYPE) {
            val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PostItemViewHolder(
                binding
            )
        } else {
            val binding = NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NetworkStateItemViewHolder(
                binding
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == Constants.POST_VIEW_TYPE) {
            (holder as PostItemViewHolder).bind(postFilterList[position],context)
            holder.itemView.setOnClickListener{
                postFilterList[position].let { it1 -> listener?.onItemClicked(it1) }
            }
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    fun setItemListener(listener: itemClickListener){
        this.listener = listener
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != Resource.Status.SUCCESS
    }

    override fun getItemCount() = postFilterList.size

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            Constants.NETWORK_VIEW_TYPE
        } else {
            Constants.POST_VIEW_TYPE
        }
    }

    class PostItemViewHolder (private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostDetail?, context: Context) {
            binding.tvTitle.text = post?.title?:""
            binding.tvAuthor.text = post?.author?:""
            binding.tvDate.text = post?.date?:""
            binding.tvContent.text = post?.content?.take(70) ?: ("" + "...")
          }

    }

    class NetworkStateItemViewHolder (private val binding: NetworkStateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(networkState: Resource.Status?) {
            if (networkState != null && networkState == Resource.Status.LOADING) {
                binding.progressBarItem.visibility = View.VISIBLE
            }
            else  {
                binding.progressBarItem.visibility = View.GONE
            }
            if (networkState != null && networkState == Resource.Status.ERROR) {
                binding.errorMsgItem.visibility = View.VISIBLE
            }
            else {
                binding.errorMsgItem.visibility = View.GONE
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = ArrayList<PostDetail>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(postList)
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (item in postList) {
                        if (item.title?.lowercase()?.contains(filterPattern) == true ||
                            item.author?.lowercase()?.contains(filterPattern)== true ||
                            item.content?.lowercase()?.contains(filterPattern)== true
                        ) {
                            filteredList.add(item)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                postFilterList.clear()
                postFilterList.addAll(results?.values as ArrayList<PostDetail>)
                notifyDataSetChanged()
            }
        }
    }

    fun addItems(list: List<PostDetail>){
        postList.clear()
        postList.addAll(list)
        postFilterList.clear()
        postFilterList.addAll(list)
        notifyDataSetChanged()
    }


    interface itemClickListener{
        fun onItemClicked(post:PostDetail)
    }

}
