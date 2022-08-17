package com.example.usedtradeapp.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.usedtradeapp.chatroom.ChatRoomActivity
import com.example.usedtradeapp.databinding.FragmentChatlistBinding
import com.example.usedtradeapp.firebase.DBKey.Companion.CHILD_CHAT
import com.example.usedtradeapp.firebase.DBKey.Companion.DB_USERS
import com.example.usedtradeapp.firebase.FirebaseManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment() {
    // 바인딩 객체 타입에 ?를 붙여서 null을 허용 해줘야한다. ( onDestroy 될 때 완벽하게 제거를 하기위해 )
    private var mBinding: FragmentChatlistBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    private lateinit var chatListAdapter: ChatListAdapter
    private val firebaseManager = FirebaseManager()
    private val chatRoomList = mutableListOf<ChatListItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 액티비티 와는 다르게 layoutInflater 를 쓰지 않고 inflater 인자를 가져와 뷰와 연결한다.
        mBinding = FragmentChatlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()

    }

    private fun initFragment() {
        initAdapter()
        initFirebase()
        onClickEvent()
    }

    private fun initAdapter() = with(binding) {
        chatRoomList.clear()
        chatListAdapter = ChatListAdapter {
            goChatRoom(it)
        }
        chatListRecyclerView.apply {
            adapter = chatListAdapter
        }
    }

    private fun initFirebase() {
        val chatDB = Firebase.database.reference.child(DB_USERS).child(firebaseManager.auth.currentUser?.uid.orEmpty()).child(CHILD_CHAT)

        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }

                chatListAdapter.submitList(chatRoomList)
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    private fun goChatRoom(chatListItem: ChatListItem) {
        context?.let {
            val intent = Intent(it, ChatRoomActivity::class.java)
            intent.putExtra("chatKey", chatListItem.key)
            startActivity(intent)
        }
    }

    private fun onClickEvent() = with(binding) {

    }

    // 프래그먼트가 destroy (파괴) 될때..
    override fun onDestroyView() {
        // onDestroyView 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroyView()
    }
}