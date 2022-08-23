package com.example.usedtradeapp.firebase

import com.example.usedtradeapp.firebase.DBKey.Companion.DB_ARTICLES
import com.example.usedtradeapp.firebase.DBKey.Companion.DB_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class FirebaseManager {
    val articleDB: DatabaseReference = Firebase.database.reference.child(DB_ARTICLES)
    val userDB: DatabaseReference = Firebase.database.reference.child(DB_USERS)
    val auth: FirebaseAuth = Firebase.auth
    val storage: FirebaseStorage = Firebase.storage

    fun addArticleDBListener(listener: ChildEventListener) {
        articleDB.addChildEventListener(listener)
    }

    fun removeArticleDBListener(listener: ChildEventListener) {
        articleDB.removeEventListener(listener)
    }
}