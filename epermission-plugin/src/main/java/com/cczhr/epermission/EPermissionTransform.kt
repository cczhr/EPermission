package com.cczhr.epermission


import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils

import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*

import java.io.File
import java.io.FileOutputStream


/**
 * @author cczhr
 * @description
 * @since 2021/4/26 14:49
 */
class EPermissionTransform() : Transform() {
    override fun getName(): String = "EPermissionTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS


    //指明当前Transform是否支持增量编译
    override fun isIncremental(): Boolean = false
    //    指Transform要操作内容的范围，官方文档Scope有7种类型：
    //    EXTERNAL_LIBRARIES        只有外部库
    //    PROJECT                       只有项目内容
    //    PROJECT_LOCAL_DEPS            只有项目的本地依赖(本地jar)
    //    PROVIDED_ONLY                 只提供本地或远程依赖项
    //    SUB_PROJECTS              只有子项目。
    //    SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
    //    TESTED_CODE                   由当前变量(包括依赖项)测试的代码
    //    SCOPE_FULL_PROJECT        整个项目
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }


    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        val inputs = transformInvocation.inputs//获取输入数据 jar包 和 文件目录
        val outputProvider = transformInvocation.outputProvider//输出路径  用于将修改的文件复制到输出目录
        //删除之前的输出
        outputProvider?.deleteAll()
        inputs.forEach { transformInput ->
            //文件目录下的class文件
            transformInput.directoryInputs.forEach { directoryInput ->
                val fileTree: FileTreeWalk = directoryInput.file.walk()
                //过滤 class文件
                fileTree.maxDepth(Int.MAX_VALUE).filter { it.isFile }.filter { it.extension=="class"}.forEach { file ->

                    EPermissionPlugin.directoryPath=file.parent
                    val classReader=ClassReader(file.readBytes())
                    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    val ePermissionClassVisitor= EPermissionClassVisitor(classWriter)
                    classReader.accept(ePermissionClassVisitor,0)
                    val bytes = classWriter.toByteArray()
                    val fos = FileOutputStream(file)
                    fos.write(bytes)
                    fos.flush()
                    fos.close()
                }
                val dest= outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)

            }
            //文件目录下的Jar
            transformInput.jarInputs.forEach {jarInput->
                val dest = outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }


        }

    }

}