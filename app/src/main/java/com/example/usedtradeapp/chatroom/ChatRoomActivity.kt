package com.example.usedtradeapp.chatroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.usedtradeapp.databinding.ActivityChatroomBinding
import com.example.usedtradeapp.firebase.DBKey.Companion.DB_CHATS
import com.example.usedtradeapp.firebase.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRoomActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private var mBinding: ActivityChatroomBinding? = null
    //매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!
    private val firebaseManager = FirebaseManager()
    private var chatDB: DatabaseReference? = null
    private val chatList = mutableListOf<ChatItem>()
    private lateinit var chatItemAdapter: ChatItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActivity()
    }

    private fun initActivity() {
        initAdapter()
        initFirebase()
        onClickEvent()
    }

    private fun initAdapter() = with(binding) {
        chatItemAdapter = ChatItemAdapter()
        chatRecyclerView.apply {
            adapter = chatItemAdapter
        }
    }

    private fun initFirebase() {
        val chatKey = intent.getLongExtra("chatKey", -1)

        chatDB = Firebase.database.reference.child(DB_CHATS).child("$chatKey")

        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                chatItemAdapter.submitList(chatList)
                chatItemAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}


        })

    }

    private fun onClickEvent() = with(binding) {
        sendButton.setOnClickListener {
            val chatItem = ChatItem(
                senderId = auth.currentUser?.uid.orEmpty(),
                message = messageEditText.text.toString()
            )

            chatDB?.push()?.setValue(chatItem)
        }
    }

    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroy()
    }

}