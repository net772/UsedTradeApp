package com.example.usedtradeapp.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.usedtradeapp.databinding.ItemChatListBinding

class ChatListAdapter(val onItemClicked: (ChatListItem) -> Unit) : ListAdapter<ChatListItem, ChatListAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(private val binding: ItemChatListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatListItem: ChatListItem) = with(binding) {

            chatRoomTitleTextView.text = chatListItem.itemTitle

        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatListItem>() {
            override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}