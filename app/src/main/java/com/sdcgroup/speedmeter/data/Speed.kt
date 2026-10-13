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

private fun scaledMagnitude(value: Double, base: Double, units: List<String>): Pair<String, String> {
    var scaledValue = value
    var unitIndex = 0

    while (scaledValue >= base && unitIndex < units.size - 1) {
        scaledValue /= base
        unitIndex++
    }

    val decimals = if (unitIndex == 0) 0 else 1
    var formatted = String.format("%.${decimals}f", scaledValue)

    if (formatted.toDouble() >= base && unitIndex < units.size - 1) {
        unitIndex++
        scaledValue /= base
        val nextDecimals = if (unitIndex == 0) 0 else 1
        formatted = String.format("%.${nextDecimals}f", scaledValue)
    }

    return formatted to units[unitIndex]
}

fun formatSpeed(bytesPerSecond: Long, unit: SpeedUnit = SpeedUnit.BYTES_PER_SECOND): HumanSpeed {
    val safeBytes = if (bytesPerSecond < 0) 0L else bytesPerSecond

    return when (unit) {
        SpeedUnit.BYTES_PER_SECOND -> {
            val (value, label) = scaledMagnitude(
                value = safeBytes.toDouble(),
                base = 1024.0,
                units = listOf("B/s", "KB/s", "MB/s", "GB/s")
            )
            HumanSpeed(speedValue = value, speedUnit = label)
        }
        SpeedUnit.BITS_PER_SECOND -> {
            val bitsPerSecond = safeBytes * 8.0
            val (value, label) = scaledMagnitude(
                value = bitsPerSecond,
                base = 1000.0,
                units = listOf("b/s", "Kb/s", "Mb/s", "Gb/s")
            )
            HumanSpeed(speedValue = value, speedUnit = label)
        }
    }
}

fun formatDataUsage(bytes: Long, unit: DataUnit = DataUnit.BYTES_BINARY): String {
    val safeBytes = if (bytes < 0) 0L else bytes

    val (value, label) = when (unit) {
        DataUnit.BITS_DECIMAL -> scaledMagnitude(
            value = safeBytes * 8.0,
            base = 1000.0,
            units = listOf("b", "Kb", "Mb", "Gb", "Tb")
        )
        DataUnit.BYTES_DECIMAL -> scaledMagnitude(
            value = safeBytes.toDouble(),
            base = 1000.0,
            units = listOf("B", "kB", "MB", "GB", "TB")
        )
        DataUnit.BYTES_BINARY -> scaledMagnitude(
            value = safeBytes.toDouble(),
            base = 1024.0,
            units = listOf("B", "KiB", "MiB", "GiB", "TiB")
        )
    }

    return "$value $label"
}
