package com.example.usedtradeapp.permission

interface PermissionListener {
    fun onPermissionGranted()
    fun onPermissionDenied()
}