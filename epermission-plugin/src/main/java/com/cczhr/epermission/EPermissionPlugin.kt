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
        var directoryPath: String = ""
    }
    override fun apply(project: Project) {
        directoryPath = ""
        project.extensions.getByType(AppExtension::class.java).registerTransform(EPermissionTransform())
    }
}