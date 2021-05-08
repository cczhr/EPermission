package com.cczhr.epermission

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * @author cczhr
 * @description
 * @since 2021/4/26 13:44
 */
class EPermissionPlugin : Plugin<Project> {
    companion object {
        val permissions = ArrayList<String>()
        var directoryPath: String = ""
    }

    private fun getPermissions(manifest: String, result: ArrayList<String>) {
        val start = manifest.indexOf("<uses-permission")
        if (start == -1)
            return
        val end = manifest.indexOf("/>", start);
        if (end == -1)
            return
        val permission = manifest.substring(start, end)
                .replace(" ", "")
                .replace("\r ", "")
                .replace("\n ", "")
                .replace("<uses-permissionandroid:name=", "")
                .replace("\"", "")
                .replace("/>", "")

        val dealManifest = manifest.replace(manifest.substring(start, end + 1), "")
        result.add(permission)
        getPermissions(dealManifest, result)
    }


    override fun apply(project: Project) {
        permissions.clear()
        directoryPath = ""
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension.applicationVariants.all { variant ->
            variant.outputs.all { output ->
                val processManifest = output.processManifestProvider.get()
                processManifest.doLast {
                    val manifestOutputDirectory = it.outputs.files.asPath
                    val manifestOutFile = File("$manifestOutputDirectory${File.separator}AndroidManifest.xml")
                    getPermissions(manifestOutFile.readText(), permissions)
                    permissions.forEach { permission ->
                        println("EPermissionPlugin-permission:$permission")
                    }
                }
            }

        }
        appExtension.registerTransform(EPermissionTransform())


    }
}