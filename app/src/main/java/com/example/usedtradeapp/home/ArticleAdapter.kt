package com.example.usedtradeapp.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.usedtradeapp.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(
    val onItemClicked: (ArticleModel) -> Unit
): ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).also {  holder ->
            binding.root.setOnClickListener {
                if (RecyclerView.NO_POSITION != holder.adapterPosition) {
                    onItemClicked(currentList[holder.adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(articleModel: ArticleModel) = with(binding) {
            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(articleModel.createdAt)

            titleTextView.text = articleModel.title
            dateTextView.text = format.format(date).toString()
            priceTextView.text = articleModel.price

            if (articleModel.imageUrl.isNotEmpty()) {
                Glide.with(thumbnailImageView)
                    .load(articleModel.imageUrl)
                    .into(thumbnailImageView)
            }
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<ArticleModel>,
        currentList: MutableList<ArticleModel>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        Log.d("동현","previousList : $previousList")
        Log.d("동현","currentList : $currentList")
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                Log.d("동현","areItemsTheSame")
                Log.d("동현","areItemsTheSame : ${oldItem.createdAt == newItem.createdAt}")
                return oldItem.createdAt == newItem.createdAt
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                Log.d("동현","areContentsTheSame")
                Log.d("동현","areContentsTheSame : ${oldItem == newItem}")

                return oldItem == newItem
            }

        }
    }

}

//override fun getBinding(
//    parent: ViewGroup,
//    viewType: Int,
//): BaseViewHolder<ItemHistoryLayoutBinding> {
//    val binding = ItemHistoryLayoutBinding.inflate(
//        LayoutInflater.from(parent.context), parent, false)
//
//    return ViewHolder(binding).also { holder ->
//        binding.btnRemove.setOnClickListener {
//            if (RecyclerView.NO_POSITION != holder.adapterPosition) {
//                removeItem(holder.adapterPosition)
//            }
//        }
//    }
//}
