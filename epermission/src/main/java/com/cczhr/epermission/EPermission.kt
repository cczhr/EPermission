package com.cczhr.epermission

/**
 * @author cczhr
 * @description
 * @since 2021/4/29 09:41
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class EPermission(val value: Boolean =false) {
    companion object{
        const val REQUEST_CODE=100
    }

}
