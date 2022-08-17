package com.example.usedtradeapp.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.usedtradeapp.chatlist.ChatListItem
import com.example.usedtradeapp.databinding.FragmentHomeBinding
import com.example.usedtradeapp.firebase.DBKey.Companion.CHILD_CHAT
import com.example.usedtradeapp.firebase.FirebaseManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class HomeFragment : Fragment() {
    // 바인딩 객체 타입에 ?를 붙여서 null을 허용 해줘야한다. ( onDestroy 될 때 완벽하게 제거를 하기위해 )
    private var mBinding: FragmentHomeBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    private lateinit var articleAdapter: ArticleAdapter

    private val firebaseManager = FirebaseManager()
    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) = Unit
        override fun onChildRemoved(snapshot: DataSnapshot) = Unit
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) = Unit
        override fun onCancelled(error: DatabaseError) = Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 액티비티 와는 다르게 layoutInflater 를 쓰지 않고 inflater 인자를 가져와 뷰와 연결한다.
        mBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    private fun initFragment() {
        initAdpater()
        initFirebase()
        onClickEvent()
    }

    private fun initFirebase() {
        firebaseManager.addArticleDBListener(listener)
    }

    private fun initAdpater() = with(binding) {
        articleList.clear()
        articleAdapter = ArticleAdapter { createChatRoom(it) }
        articleRecyclerView.apply {
            adapter = articleAdapter
        }
    }

    private fun onClickEvent() = with(binding) {
        addFloatingButton.setOnClickListener {
            if (firebaseManager.auth.currentUser != null) {
                val intent = Intent(requireContext(), AddArticleActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "로그인 후 사용해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createChatRoom(articleModel: ArticleModel) {
        if (firebaseManager.auth.currentUser != null) {
            // 로그인을 한 상태
            if (firebaseManager.auth.uid != articleModel.sellerId) { // 상대방 게시물이라면

                val chatRoom = ChatListItem(
                    buyerId = firebaseManager.auth.currentUser?.uid.orEmpty(),
                    sellerId = articleModel.sellerId,
                    itemTitle = articleModel.title,
                    key = System.currentTimeMillis()
                )
                firebaseManager.userDB.child(firebaseManager.auth.currentUser?.uid.orEmpty())
                    .child(CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                firebaseManager.userDB.child(articleModel.sellerId)
                    .child(CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                Toast.makeText(requireContext(), "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.", Toast.LENGTH_SHORT).show()

            } else {
                // 내가 올린 아이템
                Toast.makeText(requireContext(), "내가 올린 아이템입니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 로그인 x
            Toast.makeText(requireContext(), "로그인 후 사용해주세요", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
     //   Log.d("동현","list : $articleList")
      //  articleAdapter.notifyDataSetChanged()
    }

    // 프래그먼트가 destroy (파괴) 될때..
    override fun onDestroyView() {
        // onDestroyView 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        firebaseManager.removeArticleDBListener(listener)
        super.onDestroyView()
    }
}