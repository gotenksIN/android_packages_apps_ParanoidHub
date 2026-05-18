/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-FileCopyrightText: The Paranoid Android Project
 * SPDX-License-Identifier: Apache-2.0
 */

package co.aospa.hub.util

import co.aospa.hub.data.Update
import co.aospa.hub.deviceinfo.DeviceInfoUtils
import co.aospa.hub.misc.Utils
import java.io.File

object InstallUtils {
    @JvmStatic
    fun isScratchMounted() = runCatching {
        File("/proc/mounts").useLines { lines ->
            lines.any { it.split(" ")[1] == "/mnt/scratch" }
        }
    }.getOrDefault(false)

    enum class BlockedReason {
        NONE, DOWNGRADE, VERSION_UNSUPPORTED
    }

    @JvmStatic
    fun getBlockedReason(update: Update) = when {
        !DeviceInfoUtils.isDowngradingAllowed && !Utils.isUpdateNewer(
                update.timestamp,
                update.version,
                DeviceInfoUtils.buildDateTimestamp,
                DeviceInfoUtils.buildVersionIncremental,
            )
            -> BlockedReason.DOWNGRADE

        else -> BlockedReason.NONE
    }

    @JvmStatic
    fun canInstall(update: Update) = getBlockedReason(update) == BlockedReason.NONE
}
