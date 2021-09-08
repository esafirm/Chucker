package com.chucker.tracker.internal.support

import android.util.Log
import java.io.File
import java.io.IOException
import java.util.concurrent.atomic.AtomicLong

internal object FileFactory {
    private val uniqueIdGenerator = AtomicLong()

    fun create(directory: File) = create(directory, fileName = "chucker-tracker-${uniqueIdGenerator.getAndIncrement()}")

    fun create(directory: File, fileName: String): File? = try {
        File(directory, fileName).apply {
            if (exists() && !delete()) {
                throw IOException("Failed to delete file $this")
            }
            parentFile?.mkdirs()
            if (!createNewFile()) {
                throw IOException("File $this already exists")
            }
        }
    } catch (e: IOException) {
        Log.w("tracker_log", "An error occurred while creating a file", e)
        null
    }
}
