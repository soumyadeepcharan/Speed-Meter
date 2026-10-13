package com.sdcgroup.speedmeter.data

data class HumanSpeed(
    val speedValue: String,
    val speedUnit: String
)

data class Speed(
    val down: HumanSpeed,
    val up: HumanSpeed,
    val total: HumanSpeed
) {
    fun getHumanSpeed(speedType: String): HumanSpeed {
        return when (speedType) {
            "download" -> down
            "upload" -> up
            else -> total
        }
    }
}

fun formatSpeed(bytesPerSecond: Long): HumanSpeed {
    val safeBytes = if (bytesPerSecond < 0) 0L else bytesPerSecond
    val kbPerSecond = safeBytes / 1024.0
    return if (kbPerSecond < 1024.0) {
        HumanSpeed(
            speedValue = String.format("%.0f", kbPerSecond),
            speedUnit = "KB/s"
        )
    } else {
        val mbPerSecond = kbPerSecond / 1024.0
        HumanSpeed(
            speedValue = String.format("%.1f", mbPerSecond),
            speedUnit = "MB/s"
        )
    }
}

fun formatDataUsage(bytes: Long): String {
    val safeBytes = if (bytes < 0) 0L else bytes
    if (safeBytes < 1024L) {
        return String.format("%d B", safeBytes)
    }
    val kb = safeBytes / 1024.0
    if (kb < 1024.0) {
        return String.format("%.1f KB", kb)
    }
    val mb = kb / 1024.0
    if (mb < 1024.0) {
        return String.format("%.2f MiB", mb)
    }
    val gb = mb / 1024.0
    return String.format("%.2f GiB", gb)
}
