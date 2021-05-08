package com.cczhr.epermissiondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cczhr.epermission.EPermission

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
    }

    @EPermission//只负责申请权限
    fun requestPermissions() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EPermission.REQUEST_CODE) {
            //自己处理权限回调
        }
    }
}