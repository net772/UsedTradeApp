package com.example.usedtradeapp.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.usedtradeapp.databinding.FragmentMypageBinding
import com.example.usedtradeapp.firebase.FirebaseManager

class MyPageFragment: Fragment(){
    // 바인딩 객체 타입에 ?를 붙여서 null을 허용 해줘야한다. ( onDestroy 될 때 완벽하게 제거를 하기위해 )
    private var mBinding: FragmentMypageBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    private val firebaseManager = FirebaseManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 액티비티 와는 다르게 layoutInflater 를 쓰지 않고 inflater 인자를 가져와 뷰와 연결한다.
        mBinding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    override fun onStart() {
        super.onStart()
        checkLoginState()
    }

    private fun checkLoginState() = with(binding) {
        if (firebaseManager.auth.currentUser == null) {
            emailEditText.text.clear()
            passwordEditText.text.clear()
            emailEditText.isEnabled = true
            passwordEditText.isEnabled = true
            signInOutButton.text = "로그인"
            signInOutButton.isEnabled = false
            signUpButton.isEnabled = false
        } else {
            emailEditText.setText(firebaseManager.auth.currentUser?.email.orEmpty())
            passwordEditText.setText("***********")
            emailEditText.isEnabled = false
            passwordEditText.isEnabled = false
            signInOutButton.text = "로그아웃"
            signInOutButton.isEnabled = true
            signUpButton.isEnabled = false
        }
    }

    private fun initFragment() {
        onClickEvent()
    }

    private fun onClickEvent() = with(binding) {
        signInOutButton.setOnClickListener { onClickSignInOutButton() }
        signUpButton.setOnClickListener { onClickSignUpButton() }
        emailEditText.addTextChangedListener { checkEnableButton() }
        passwordEditText.addTextChangedListener { checkEnableButton() }
    }

    private fun onClickSignInOutButton() = with(binding) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (firebaseManager.auth.currentUser == null) {
            // 로그인
            firebaseManager.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        successSignIn()
                    } else {
                        Toast.makeText(context, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            firebaseManager.auth.signOut()
            emailEditText.text.clear()
            emailEditText.isEnabled = true
            passwordEditText.text.clear()
            passwordEditText.isEnabled = true

            signInOutButton.text = "로그인"
            signInOutButton.isEnabled = false
            signUpButton.isEnabled = false
        }
    }

    private fun onClickSignUpButton() = with(binding) {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        firebaseManager.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if(task.isSuccessful) {
                    Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "회원가입에 실패했습니다. 이미 가입한 이메일일 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkEnableButton() = with(binding) {
        val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
        binding.signInOutButton.isEnabled = enable
        binding.signUpButton.isEnabled = enable
    }

    private fun successSignIn() = with(binding) {
        if (firebaseManager.auth.currentUser == null) {
            Toast.makeText(context, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        emailEditText.isEnabled = false
        passwordEditText.isEnabled = false
        signUpButton.isEnabled = false
        signInOutButton.text = "로그아웃"
    }

    // 프래그먼트가 destroy (파괴) 될때..
    override fun onDestroyView() {
        // onDestroyView 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroyView()
    }
}