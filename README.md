# 一行代码实现申请android应用权限
[![](https://jitpack.io/v/cczhr/EPermission.svg)](https://jitpack.io/#cczhr/EPermission)

## 使用说明

在Project 的 build.gradle 加上

```groovy
repositories {
    ...
	maven { url 'https://jitpack.io' }
}
 dependencies {
    ...
	classpath "com.github.cczhr.EPermission:epermission-plugin:1.0.0"
}
```

在Module的 build.gradle 加上

```groovy
plugins {
    ...
    id 'com.cczhr.epermission'
}
dependencies {
    ...
	implementation 'com.github.cczhr.EPermission:epermission:1.0.0'
}
```



在一个Activity上写一个任意名称的方法 加上`@EPermission `并调用它即可

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
    }

    @EPermission(true)//传ture会自动申请权限和弹出拒绝权限提示框
    fun requestPermissions() {
    }
    
 /*  @EPermission//只负责申请权限
    fun requestPermissions() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EPermission.REQUEST_CODE) {
            //自己处理权限回调
        }
    }*/
    
    
}
```

## 原理

 Gradle Plugin + ASM 获取合并后的Manifest文件再通过ASM写入字节码到标记的class文件中

## 注意

只支持使用 androidx 的项目
