package com.example.usedtradeapp.chatlist

import com.example.usedtradeapp.BaseFragment
import com.example.usedtradeapp.R
import com.example.usedtradeapp.databinding.FragmentChatlistBinding
import androidx.fragment.app.viewModels

class ChatListFragment : BaseFragment<FragmentChatlistBinding, ChatListViewModel>() {
    override fun getLayoutResourceId() = R.layout.fragment_chatlist
    override val mViewModel: ChatListViewModel by viewModels()

}