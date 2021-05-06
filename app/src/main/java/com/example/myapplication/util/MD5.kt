package com.example.myapplication.util

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and


 object  MD5 {

    /**
     * 将数据进行 MD5 加密，并以16进制字符串格式输出
     * @param data
     * @return
     */
    fun md5(data: String): String {
        try {
            val md5 = md5(data.toByteArray(charset("utf-8")))
            return toHexString(md5)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 将字节数组进行 MD5 加密
     * @param data
     * @return
     */
    fun md5(data: ByteArray?): ByteArray {
        try {
            val md: MessageDigest = MessageDigest.getInstance("md5")
            return md.digest(data)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }

    /**
     * 将加密后的字节数组，转换成16进制的字符串
     * @param md5
     * @return
     */
    private fun toHexString(md5: ByteArray): String {
        val sb = StringBuilder()
        for (b in md5) {
            sb.append(Integer.toHexString((b and 0xf.toByte()).toInt()))
        }
        return sb.toString()
    }



}