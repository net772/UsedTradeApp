package com.example.usedtradeapp.chatlist

data class ChatListItem(
    val buyerId: String = "",
    val sellerId: String = "",
    val itemTitle: String = "",
    val key: Long = 0
)