package com.cczhr.epermission

import jdk.internal.org.objectweb.asm.Opcodes.*
import org.objectweb.asm.*
import java.io.File
import java.io.FileOutputStream


/**
 * @author cczhr
 * @description
 * @since 2021/4/27
 */
class EPermissionMethodVisitor(private val classVisitor: ClassVisitor, private val methodVisitor: MethodVisitor, val className: String, val methodName: String) : MethodVisitor(Opcodes.ASM7, methodVisitor) {
    companion object {
        private const val E_PERMISSION = "Lcom/cczhr/epermission/EPermission;"
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        if (descriptor == E_PERMISSION) {
            println("EPermission:inject class:$className  method:$methodName")
            injectEPermission()
            return object : AnnotationVisitor(Opcodes.ASM7, mv.visitAnnotation(descriptor, visible)) {
                override fun visit(name: String?, value: Any?) {
                    if (descriptor == E_PERMISSION) {
                        val isInjectEPermissionResult = value as Boolean?
                        isInjectEPermissionResult?.let {
                            if (it) {
                                injectInnerClass()
                                injectEPermissionResult()

                            }
                        }
                    }
                    super.visit(name, value)
                }
            }
        }
        return super.visitAnnotation(descriptor, visible)
    }


    private fun injectInnerClass() {
        val classWriter = ClassWriter(0)
        var methodVisitor: MethodVisitor
        var fieldVisitor: FieldVisitor
        classWriter.visit(V1_8, ACC_PUBLIC or ACC_SUPER, "${className}\$EPermissionOnClick", null, "java/lang/Object", arrayOf("android/content/DialogInterface\$OnClickListener"))
        classWriter.visitInnerClass("android/content/DialogInterface\$OnClickListener", "android/content/DialogInterface", "OnClickListener", ACC_PUBLIC or ACC_STATIC or ACC_ABSTRACT or ACC_INTERFACE)

        run {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE, "thisActivity", "L${className};", null, null)
            fieldVisitor.visitEnd()
        }
        run {
            methodVisitor = classWriter.visitMethod(0, "<init>", "(L${className};)V", null, null)
            methodVisitor.visitCode()
            val label0 = Label()
            methodVisitor.visitLabel(label0)
            methodVisitor.visitLineNumber(14, label0)
            methodVisitor.visitVarInsn(ALOAD, 0)
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
            val label1 = Label()
            methodVisitor.visitLabel(label1)
            methodVisitor.visitLineNumber(15, label1)
            methodVisitor.visitVarInsn(ALOAD, 0)
            methodVisitor.visitVarInsn(ALOAD, 1)
            methodVisitor.visitFieldInsn(PUTFIELD, "${className}\$EPermissionOnClick", "thisActivity", "L${className};")
            val label2 = Label()
            methodVisitor.visitLabel(label2)
            methodVisitor.visitLineNumber(16, label2)
            methodVisitor.visitInsn(RETURN)
            val label3 = Label()
            methodVisitor.visitLabel(label3)
            methodVisitor.visitLocalVariable("this", "L${className}\$EPermissionOnClick;", null, label0, label3, 0)
            methodVisitor.visitLocalVariable("this$0", "L${className};", null, label0, label3, 1)
            methodVisitor.visitMaxs(2, 2)
            methodVisitor.visitEnd()
        }
        run {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "onClick", "(Landroid/content/DialogInterface;I)V", null, null)
            methodVisitor.visitCode()
            val label0 = Label()
            methodVisitor.visitLabel(label0)
            methodVisitor.visitLineNumber(20, label0)
            methodVisitor.visitTypeInsn(NEW, "android/content/Intent")
            methodVisitor.visitInsn(DUP)
            methodVisitor.visitLdcInsn("android.settings.APPLICATION_DETAILS_SETTINGS")
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "android/content/Intent", "<init>", "(Ljava/lang/String;)V", false)
            methodVisitor.visitVarInsn(ASTORE, 3)
            val label1 = Label()
            methodVisitor.visitLabel(label1)
            methodVisitor.visitLineNumber(21, label1)
            methodVisitor.visitVarInsn(ALOAD, 3)
            methodVisitor.visitLdcInsn(268435456)
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/content/Intent", "addFlags", "(I)Landroid/content/Intent;", false)
            methodVisitor.visitInsn(POP)
            val label2 = Label()
            methodVisitor.visitLabel(label2)
            methodVisitor.visitLineNumber(22, label2)
            methodVisitor.visitVarInsn(ALOAD, 3)
            methodVisitor.visitLdcInsn("package")
            methodVisitor.visitVarInsn(ALOAD, 0)
            methodVisitor.visitFieldInsn(GETFIELD, "${className}\$EPermissionOnClick", "thisActivity", "L${className};")
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, "getApplicationInfo", "()Landroid/content/pm/ApplicationInfo;", false)
            methodVisitor.visitFieldInsn(GETFIELD, "android/content/pm/ApplicationInfo", "packageName", "Ljava/lang/String;")
            methodVisitor.visitInsn(ACONST_NULL)
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/net/Uri", "fromParts", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri;", false)
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/content/Intent", "setData", "(Landroid/net/Uri;)Landroid/content/Intent;", false)
            methodVisitor.visitInsn(POP)
            val label3 = Label()
            methodVisitor.visitLabel(label3)
            methodVisitor.visitLineNumber(23, label3)
            methodVisitor.visitVarInsn(ALOAD, 0)
            methodVisitor.visitFieldInsn(GETFIELD, "${className}\$EPermissionOnClick", "thisActivity", "L${className};")
            methodVisitor.visitVarInsn(ALOAD, 3)
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, "startActivity", "(Landroid/content/Intent;)V", false)
            val label4 = Label()
            methodVisitor.visitLabel(label4)
            methodVisitor.visitLineNumber(24, label4)
            methodVisitor.visitInsn(RETURN)
            val label5 = Label()
            methodVisitor.visitLabel(label5)
            methodVisitor.visitLocalVariable("this", "L${className}\$EPermissionOnClick;", null, label0, label5, 0)
            methodVisitor.visitLocalVariable("dialog", "Landroid/content/DialogInterface;", null, label0, label5, 1)
            methodVisitor.visitLocalVariable("which", "I", null, label0, label5, 2)
            methodVisitor.visitLocalVariable("intent", "Landroid/content/Intent;", null, label1, label5, 3)
            methodVisitor.visitMaxs(4, 4)
            methodVisitor.visitEnd()
        }
        classWriter.visitEnd()

        val tempArr = className.split("/")
        val fos = FileOutputStream(File(EPermissionPlugin.directoryPath + File.separator + "${tempArr[tempArr.size - 1]}\$EPermissionOnClick.class"))
        fos.write(classWriter.toByteArray())
        fos.flush()
        fos.close()


    }

    private fun injectEPermissionResult() {
        classVisitor.visitInnerClass("$className\$EPermissionOnClick", className, "EPermissionOnClick", 0);
        val methodVisitor: MethodVisitor = classVisitor.visitMethod(ACC_PUBLIC, "onRequestPermissionsResult", "(I[Ljava/lang/String;[I)V", null, null);
        methodVisitor.visitCode();

        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(56, label0)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitVarInsn(ILOAD, 1)
        methodVisitor.visitVarInsn(ALOAD, 2)
        methodVisitor.visitVarInsn(ALOAD, 3)
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "androidx/appcompat/app/AppCompatActivity", "onRequestPermissionsResult", "(I[Ljava/lang/String;[I)V", false)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(57, label1)
        methodVisitor.visitVarInsn(ILOAD, 1)
        methodVisitor.visitIntInsn(BIPUSH, 100)
        val label2 = Label()
        methodVisitor.visitJumpInsn(IF_ICMPNE, label2)
        val label3 = Label()
        methodVisitor.visitLabel(label3)
        methodVisitor.visitLineNumber(58, label3)
        methodVisitor.visitVarInsn(ALOAD, 3)
        methodVisitor.visitVarInsn(ASTORE, 4)
        methodVisitor.visitVarInsn(ALOAD, 4)
        methodVisitor.visitInsn(ARRAYLENGTH)
        methodVisitor.visitVarInsn(ISTORE, 5)
        methodVisitor.visitInsn(ICONST_0)
        methodVisitor.visitVarInsn(ISTORE, 6)
        val label4 = Label()
        methodVisitor.visitLabel(label4)
        methodVisitor.visitFrame(Opcodes.F_APPEND, 3, arrayOf("[I", Opcodes.INTEGER, Opcodes.INTEGER), 0, null)
        methodVisitor.visitVarInsn(ILOAD, 6)
        methodVisitor.visitVarInsn(ILOAD, 5)
        methodVisitor.visitJumpInsn(IF_ICMPGE, label2)
        methodVisitor.visitVarInsn(ALOAD, 4)
        methodVisitor.visitVarInsn(ILOAD, 6)
        methodVisitor.visitInsn(IALOAD)
        methodVisitor.visitVarInsn(ISTORE, 7)
        val label5 = Label()
        methodVisitor.visitLabel(label5)
        methodVisitor.visitLineNumber(59, label5)
        methodVisitor.visitVarInsn(ILOAD, 7)
        methodVisitor.visitInsn(ICONST_M1)
        val label6 = Label()
        methodVisitor.visitJumpInsn(IF_ICMPNE, label6)
        val label7 = Label()
        methodVisitor.visitLabel(label7)
        methodVisitor.visitLineNumber(60, label7)
        methodVisitor.visitTypeInsn(NEW, "androidx/appcompat/app/AlertDialog\$Builder")
        methodVisitor.visitInsn(DUP)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "androidx/appcompat/app/AlertDialog\$Builder", "<init>", "(Landroid/content/Context;)V", false)
        methodVisitor.visitLdcInsn("\u83b7\u53d6\u5e94\u7528\u6743\u9650\u5931\u8d25,\u8bf7\u5728\u8bbe\u7f6e-\u5e94\u7528\u6743\u9650\u4e2d\u8d4b\u4e88\u5e94\u7528\u6743\u9650")
        val label8 = Label()
        methodVisitor.visitLabel(label8)
        methodVisitor.visitLineNumber(61, label8)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "androidx/appcompat/app/AlertDialog\$Builder", "setMessage", "(Ljava/lang/CharSequence;)Landroidx/appcompat/app/AlertDialog\$Builder;", false)
        methodVisitor.visitInsn(ICONST_0)
        val label9 = Label()
        methodVisitor.visitLabel(label9)
        methodVisitor.visitLineNumber(62, label9)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "androidx/appcompat/app/AlertDialog\$Builder", "setCancelable", "(Z)Landroidx/appcompat/app/AlertDialog\$Builder;", false)
        methodVisitor.visitLdcInsn("\u53bb\u8bbe\u7f6e")
        methodVisitor.visitTypeInsn(NEW, "${className}\$EPermissionOnClick")
        methodVisitor.visitInsn(DUP)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "${className}\$EPermissionOnClick", "<init>", "(L$className;)V", false)
        val label10 = Label()
        methodVisitor.visitLabel(label10)
        methodVisitor.visitLineNumber(63, label10)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "androidx/appcompat/app/AlertDialog\$Builder", "setPositiveButton", "(Ljava/lang/CharSequence;Landroid/content/DialogInterface\$OnClickListener;)Landroidx/appcompat/app/AlertDialog\$Builder;", false)
        methodVisitor.visitLdcInsn("\u53d6\u6d88")
        methodVisitor.visitInsn(ACONST_NULL)
        val label11 = Label()
        methodVisitor.visitLabel(label11)
        methodVisitor.visitLineNumber(71, label11)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "androidx/appcompat/app/AlertDialog\$Builder", "setNegativeButton", "(Ljava/lang/CharSequence;Landroid/content/DialogInterface\$OnClickListener;)Landroidx/appcompat/app/AlertDialog\$Builder;", false)
        val label12 = Label()
        methodVisitor.visitLabel(label12)
        methodVisitor.visitLineNumber(72, label12)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "androidx/appcompat/app/AlertDialog\$Builder", "create", "()Landroidx/appcompat/app/AlertDialog;", false)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "androidx/appcompat/app/AlertDialog", "show", "()V", false)
        val label13 = Label()
        methodVisitor.visitLabel(label13)
        methodVisitor.visitLineNumber(73, label13)
        methodVisitor.visitJumpInsn(GOTO, label2)
        methodVisitor.visitLabel(label6)
        methodVisitor.visitLineNumber(58, label6)
        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
        methodVisitor.visitIincInsn(6, 1)
        methodVisitor.visitJumpInsn(GOTO, label4)
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLineNumber(79, label2)
        methodVisitor.visitFrame(Opcodes.F_CHOP, 3, null, 0, null)
        methodVisitor.visitInsn(RETURN)
        val label14 = Label()
        methodVisitor.visitLabel(label14)
        methodVisitor.visitLocalVariable("grantResult", "I", null, label5, label6, 7)
        methodVisitor.visitLocalVariable("this", "L$className;", null, label0, label14, 0)
        methodVisitor.visitLocalVariable("requestCode", "I", null, label0, label14, 1)
        methodVisitor.visitLocalVariable("permissions", "[Ljava/lang/String;", null, label0, label14, 2)
        methodVisitor.visitLocalVariable("grantResults", "[I", null, label0, label14, 3)
        methodVisitor.visitMaxs(5, 8)
        methodVisitor.visitEnd()

    }




    private fun injectEPermission(){
        val label0 = Label()
        val label1 = Label()
        val label2 = Label()
        methodVisitor.visitTryCatchBlock(label0, label1, label2, "android/content/pm/PackageManager\$NameNotFoundException")
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(20, label0)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, "getPackageManager", "()Landroid/content/pm/PackageManager;", false)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, className, "getPackageName", "()Ljava/lang/String;", false)
        methodVisitor.visitIntInsn(SIPUSH, 4096)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/content/pm/PackageManager", "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;", false)
        methodVisitor.visitFieldInsn(GETFIELD, "android/content/pm/PackageInfo", "requestedPermissions", "[Ljava/lang/String;")
        methodVisitor.visitVarInsn(ASTORE, 1)
        val label3 = Label()
        methodVisitor.visitLabel(label3)
        methodVisitor.visitLineNumber(21, label3)
        methodVisitor.visitVarInsn(ALOAD, 1)
        methodVisitor.visitJumpInsn(IFNULL, label1)
        methodVisitor.visitVarInsn(ALOAD, 1)
        methodVisitor.visitInsn(ARRAYLENGTH)
        methodVisitor.visitJumpInsn(IFLE, label1)
        val label4 = Label()
        methodVisitor.visitLabel(label4)
        methodVisitor.visitLineNumber(22, label4)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitVarInsn(ALOAD, 1)
        methodVisitor.visitIntInsn(BIPUSH, 100)
        methodVisitor.visitMethodInsn(INVOKESTATIC, "androidx/core/app/ActivityCompat", "requestPermissions", "(Landroid/app/Activity;[Ljava/lang/String;I)V", false)
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(25, label1)
        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
        val label5 = Label()
        methodVisitor.visitJumpInsn(GOTO, label5)
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLineNumber(23, label2)
        methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, arrayOf<Any>("android/content/pm/PackageManager\$NameNotFoundException"))
        methodVisitor.visitVarInsn(ASTORE, 1)
        val label6 = Label()
        methodVisitor.visitLabel(label6)
        methodVisitor.visitLineNumber(24, label6)
        methodVisitor.visitVarInsn(ALOAD, 1)
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "android/content/pm/PackageManager\$NameNotFoundException", "printStackTrace", "()V", false)
        methodVisitor.visitLabel(label5)
        methodVisitor.visitLineNumber(26, label5)
        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
        methodVisitor.visitInsn(RETURN)
        val label7 = Label()
        methodVisitor.visitLabel(label7)
        methodVisitor.visitLocalVariable("permissions", "[Ljava/lang/String;", null, label3, label1, 1)
        methodVisitor.visitLocalVariable("e", "Landroid/content/pm/PackageManager\$NameNotFoundException;", null, label6, label5, 1)
        methodVisitor.visitLocalVariable("this", "L${className};", null, label0, label7, 0)
        methodVisitor.visitMaxs(3, 2)
        methodVisitor.visitEnd()
    }









}

