package com.haishinkit.yuv

import android.util.Log
import com.haishinkit.codec.BufferInfo
import com.haishinkit.codec.ByteConverter

internal class ARGB8888toYUV420SemiPlanarConverter : ByteConverter {
    override fun convert(input: ByteArray, info: BufferInfo): ByteArray {
        Log.d(javaClass.name + "#convert", "$info, ${input.size}," + Thread.currentThread().name)
        var output: ByteArray = ByteArray((info.width * info.height * 3) / 2)

        var yIndex = 0
        var uvIndex = info.width * info.height
        var index = 0
        val padding = (info.rowStride - info.width * info.pixelStride) / 4

        for (j in 0 until info.height) {
            for (i in 0 until info.width) {
                val r = input[index * 4].toUByte().toDouble()
                val g = input[index * 4 + 1].toUByte().toDouble()
                val b = input[index * 4 + 2].toUByte().toDouble()

                val y = (0.257 * r + 0.504 * g + 0.098 * b).toInt() + 16
                val u = (0.439 * r - 0.368 * g - 0.071 * b).toInt() + 128
                val v = (-0.148 * r - 0.291 * g + 0.439 * b).toInt() + 128

                output[yIndex++] = (if (y < 0) 0 else if (y > 255) 255 else y).toUByte().toByte()
                if (j % 2 == 0 && index % 2 == 0) {
                    output[uvIndex++] = (if (v < 0) 0 else if (v > 255) 255 else v).toUByte().toByte()
                    output[uvIndex++] = (if (u < 0) 0 else if (u > 255) 255 else u).toUByte().toByte()
                }
                index++
            }
            index += padding
        }

        return output
    }
}