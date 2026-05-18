/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-FileCopyrightText: The Paranoid Android Project
 * SPDX-License-Identifier: Apache-2.0
 */

package co.aospa.hub

import android.app.Application
import com.android.settingslib.spa.framework.common.SettingsPageProviderRepository
import com.android.settingslib.spa.framework.common.SpaEnvironment
import com.android.settingslib.spa.framework.common.SpaEnvironmentFactory
import kotlinx.coroutines.MainScope
import co.aospa.hub.data.AppStateRepository
import co.aospa.hub.data.UpdatesRepository
import co.aospa.hub.data.UserPreferencesRepository
import co.aospa.hub.data.source.local.UpdatesDatabase
import co.aospa.hub.data.source.local.UpdatesLocalDataSource
import co.aospa.hub.data.source.network.UpdatesNetworkDataSource
import co.aospa.hub.notifications.NotificationHelper
import co.aospa.hub.util.BatteryMonitor
import co.aospa.hub.util.NetworkMonitor

class UpdaterApplication : Application() {
    private val coroutineScope = MainScope()
    private val database by lazy { UpdatesDatabase.getInstance(applicationContext) }
    private val networkDataSource by lazy { UpdatesNetworkDataSource(applicationContext) }
    private val localDataSource by lazy { UpdatesLocalDataSource(database.updateDao()) }


    val batteryMonitor by lazy {
        BatteryMonitor(applicationContext, coroutineScope, userPreferencesRepository)
    }
    val networkMonitor by lazy { NetworkMonitor(applicationContext, coroutineScope) }
    val notificationHelper by lazy { NotificationHelper(applicationContext) }
    val appStateRepository by lazy { AppStateRepository(applicationContext) }
    val userPreferencesRepository by lazy { UserPreferencesRepository(applicationContext) }
    val updatesRepository by lazy {
        UpdatesRepository(
            networkMonitor = networkMonitor,
            notificationHelper = notificationHelper,
            networkDataSource = networkDataSource,
            localDataSource = localDataSource,
        )
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper.setUpNotificationChannels()
        SpaEnvironmentFactory.reset(object : SpaEnvironment(applicationContext) {
            override val pageProviderRepository = lazy {
                SettingsPageProviderRepository(emptyList())
            }
        })
    }
}
