package com.cczhr.epermission

import org.objectweb.asm.*


/**
 * @author cczhr
 * @description
 * @since 2021/4/27 09:40
 */
class EPermissionClassVisitor(val classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM7, classVisitor), Opcodes {
    lateinit var className: String
    override fun visitMethod(access: Int, name: String, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {

        return EPermissionMethodVisitor(classVisitor,cv.visitMethod(access, name, descriptor, signature, exceptions), className, name);
    }

    override fun visit(p0: Int, p1: Int, name: String, p3: String?, p4: String?, p5: Array<out String>?) {
        super.visit(p0, p1, name, p3, p4, p5)
        this.className = name
    }




}