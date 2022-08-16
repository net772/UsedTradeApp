package com.example.usedtradeapp.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.usedtradeapp.databinding.ActivityAddArticleBinding
import com.example.usedtradeapp.firebase.FirebaseManager
import com.example.usedtradeapp.permission.PermissionConstant
import com.example.usedtradeapp.permission.PermissionListener
import com.example.usedtradeapp.permission.PermissionUtil

class AddArticleActivity : AppCompatActivity() {
    private var mBinding: ActivityAddArticleBinding? = null
    //매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    private var selectedUri: Uri? = null
    private val firebaseManager = FirebaseManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용해서
        // 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActivity()
    }

    private fun initActivity() {
        onClickEvent()
    }

    private fun onClickEvent() = with(binding) {
        imageAddButton.setOnClickListener {
            PermissionUtil.easyPermission(this@AddArticleActivity,
                PermissionConstant.EXTERNAL_STORAGE_PERMISSIONS,
                object : PermissionListener {
                    override fun onPermissionDenied() {}
                    override fun onPermissionGranted() {
                        navigatePhotos()
                    }
                })
        }

        submitButton.setOnClickListener {
            progressBar.isVisible = true
            val title = titleEditText.text.toString()
            val price = priceEditText.text.toString()
            val sellerId = firebaseManager.auth.currentUser?.uid.orEmpty()

            if (selectedUri != null) {
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(
                    photoUri,
                    successHandler = { uri ->
                        uploadArticle(sellerId, title, price, uri)
                    },
                    errorHandler = {
                        Toast.makeText(this@AddArticleActivity, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                        progressBar.isVisible = false
                    }
                )
            } else {
                uploadArticle(sellerId, title, price, "")
            }


        }
    }

    private fun uploadArticle(sellerId: String, title: String, price: String, photoUri: String) {
        val model = ArticleModel(sellerId, title, System.currentTimeMillis(), "$price 원", photoUri)
        firebaseManager.articleDB.push().setValue(model)
        binding.progressBar.isVisible = false
        finish()
    }

    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        val fileName = "${System.currentTimeMillis()}.png"
        firebaseManager.storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseManager.storage.reference.child("article/photo").child(fileName)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }.addOnFailureListener { errorHandler() }
                } else {
                    errorHandler()
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.onPermissionsResult(this, requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            2000 -> {
                val uri = data?.data
                if (uri != null) {
                    binding.photoImageView.setImageURI(uri)
                    selectedUri = uri
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 스토리지 프레임워크
    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }
}