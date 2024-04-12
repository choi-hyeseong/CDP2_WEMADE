package com.home.cdp2app.health.healthconnect.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord

class HealthConnectAPI {

    // static API
    companion object {

        //healthconnect에서 사용하는 provider Package
        const val PROVIDER_PACKAGE_NAME = "com.google.android.apps.healthdata"

        //healthconnect에서 사용될 permission set. manifest에도 선언되어야 함
        val PERMISSIONS = setOf(
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getWritePermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(SleepSessionRecord::class),
            HealthPermission.getWritePermission(SleepSessionRecord::class),
            HealthPermission.getReadPermission(BloodPressureRecord::class),
            HealthPermission.getWritePermission(BloodPressureRecord::class),
        )

        /**
         * HealthConnect SDK의 상태를 반환합니다.
         * @param context Context 값입니다. Hilt - Dagger 사용시 Application Context를 사용하세요
         * @return HealthConnectStatus Enum을 반환합니다.
         */
        fun getSdkStatus(context: Context): HealthConnectStatus {
            return when (HealthConnectClient.sdkStatus(context, PROVIDER_PACKAGE_NAME)) {
                HealthConnectClient.SDK_UNAVAILABLE -> HealthConnectStatus.NOT_SUPPORTED
                HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> HealthConnectStatus.REQUIRE_INSTALL
                else -> HealthConnectStatus.OK
            }
        }

        /**
         * HealthConnect SDK 설치를 위한 Intent를 제공합니다.
         * @param context Context 값입니다. Hilt - Dagger 사용시 Application Context를 사용하세요
         * @return Intent startActivity에 사용되는 Intent를 반환합니다.
         */
        fun createInstallSdkIntent(context: Context): Intent {
            val uriString = "market://details?id=$PROVIDER_PACKAGE_NAME&url=healthconnect%3A%2F%2Fonboarding"
            return Intent(Intent.ACTION_VIEW).apply {
                setPackage("com.android.vending")
                data = Uri.parse(uriString)
                putExtra("overlay", true)
                putExtra("callerId", context.packageName)
            }
        }

        /**
         * HealthConnect Client를 가져옵니다.
         * @param context  Context 값입니다. Hilt - Dagger 사용시 Application Context를 사용하세요
         * @return HealthConnectClient를 반환합니다.
         * @throws UnsupportedOperationException - 지원하지 않는 버젼일경우 발생합니다.
         * @throws IllegalStateException - sdk가 설치되어 있지 않은경우 발생합니다.
         */
        @Throws(UnsupportedOperationException::class, IllegalStateException::class)
        fun getHealthConnectClient(context: Context): HealthConnectClient {
            return HealthConnectClient.getOrCreate(context)
        }
    }
}

/**
 * HealthConnect SDK의 상태를 나타냅니다.
 * @property NOT_SUPPORTED 지원하지 않는 기기 혹은 버젼입니다.
 * @property OK SDK가 정상 작동중입니다.
 * @property REQUIRE_INSTALL SDK의 업데이트 혹은 설치가 필요합니다.
 */
enum class HealthConnectStatus {
    NOT_SUPPORTED, OK, REQUIRE_INSTALL
}