package com.cczhr.epermissiondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cczhr.epermission.EPermission

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
    }

    @EPermission(true)//申请权限和拒绝权限弹框
    fun requestPermissions() {
    }
}