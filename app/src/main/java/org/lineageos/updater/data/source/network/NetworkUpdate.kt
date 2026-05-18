/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-FileCopyrightText: The Paranoid Android Project
 * SPDX-License-Identifier: Apache-2.0
 */

package co.aospa.hub.data.source.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import co.aospa.hub.data.Update

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class NetworkUpdate(
    @SerialName("datetime") val timestamp: String,
    @SerialName("filename") val name: String,
    @SerialName("id") val downloadId: String,
    @SerialName("build_type") val type: String,
    @SerialName("size") val fileSize: String,
    @SerialName("url") val downloadUrl: String,
    @SerialName("version_code") val version: String,
    @SerialName("android_version") val androidVersion: String,
)

@Suppress("PROVIDED_RUNTIME_TOO_LOW")
@Serializable
data class NetworkUpdateResponse(
    @SerialName("updates") val updates: List<NetworkUpdate>,
)

fun NetworkUpdate.toUpdate(): Update {
    return Update(
        downloadId = downloadId,
        name = name,
        timestamp = timestamp.toLong(),
        type = type,
        fileSize = fileSize.toLong(),
        downloadUrl = downloadUrl,
        version = version,
        isAvailableOnline = true,
    )
}
