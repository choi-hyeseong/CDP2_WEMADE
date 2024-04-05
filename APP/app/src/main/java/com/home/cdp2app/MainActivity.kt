package com.home.cdp2app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.home.cdp2app.databinding.ActivityMainBinding
import com.home.cdp2app.health.component.HealthConnectAPI
import com.home.cdp2app.health.component.HealthConnectStatus
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import com.home.cdp2app.health.healthconnect.dao.HealthDao
import com.home.cdp2app.health.heart.dao.HealthConnectHeartDao
import com.home.cdp2app.health.heart.dao.HeartDao
import com.home.cdp2app.health.heart.entity.HeartRate
import com.home.cdp2app.health.heart.mapper.HeartRateMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset

class MainActivity : AppCompatActivity() {

    private val LOG_HEADER = "MainActivity-Logger"
    private val permissions = HealthConnectAPI.PERMISSIONS
    private lateinit var requestPermission : ActivityResultLauncher<Set<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        handleHealthConnectSDK()
        initPermissionLauncher()

        CoroutineScope(Dispatchers.IO).launch {
            requestPermission()
        }

        val dao = HealthConnectDao(applicationContext)
        val heartDao = HealthConnectHeartDao(dao, HeartRateMapper())
        CoroutineScope(Dispatchers.IO).launch {
            heartDao.readHeartRate(Instant.now().minusSeconds(3600), Instant.now()).forEach {
                Log.i(LOG_HEADER, "Read Record - Time : ${it.time}, bpm : ${it.bpm}")
            }
            heartDao.writeHeartRate(listOf(
                HeartRate(Instant.now().minusSeconds(3000), 50L)
            ))
        }

    }

    //sdk init
    private fun handleHealthConnectSDK() {
        val status : HealthConnectStatus = HealthConnectAPI.getSdkStatus(this)
        Log.i(LOG_HEADER, "Health Connect Status : $status")
        //이미 설치된 경우 핸들링 안함
        if (status == HealthConnectStatus.OK)
            return

        //지원하지 않는 기기 및 버젼일경우 메시지와 함께 종료
        if (status == HealthConnectStatus.NOT_SUPPORTED) {
            Toast.makeText(this, getString(R.string.health_not_supported), Toast.LENGTH_LONG).show()
            finish()
        }
        else {
            //설치가 필요한경우 ACTION_VIEW로 호출
            Toast.makeText(this, getString(R.string.health_require_install), Toast.LENGTH_LONG).show()
            startActivity(HealthConnectAPI.createInstallSdkIntent(this))
        }
    }

    //onCreate에서 Launcher 생성하기 위한 메소드
    private fun initPermissionLauncher() {
        //요청 contract와 callback
        requestPermission = registerForActivityResult(PermissionController.createRequestPermissionResultContract()) { granted ->
            //권한이 허용되지 않았을경우
            if (!granted.containsAll(permissions)) {
                Toast.makeText(this, R.string.health_permission_denied, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    //HealthConnect SDK Permission request
    private suspend fun requestPermission() {
        //요청될 permission
        val permissions = HealthConnectAPI.PERMISSIONS
        val healthConnectClient = HealthConnectAPI.getHealthConnectClient(applicationContext) //Singleton 준수하기 위한 Application Context 사용

        //이미 충분한 권한이 지급된경우 return
        if (healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions))
            return

        requestPermission.launch(permissions)
    }
}