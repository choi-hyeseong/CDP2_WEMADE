package com.home.cdp2app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.PermissionController
import com.home.cdp2app.databinding.ActivityMainBinding
import com.home.cdp2app.health.healthconnect.component.HealthConnectAPI
import com.home.cdp2app.health.healthconnect.component.HealthConnectStatus
import com.home.cdp2app.main.MainPagerActivity
import com.home.cdp2app.common.memory.SharedPreferencesStorage
import com.home.cdp2app.user.sign.view.AuthActivity
import com.home.cdp2app.user.token.repository.PreferenceAuthTokenRepository
import com.home.cdp2app.user.token.usecase.HasAuthToken
import com.home.cdp2app.tutorial.repository.PreferenceTutorialRepository
import com.home.cdp2app.tutorial.usecase.CheckTutorialCompleted
import com.home.cdp2app.tutorial.view.TutorialActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HealthConnectSuccessCallback {

    private val LOG_HEADER = "MainActivity-Logger"
    private val permissions = HealthConnectAPI.PERMISSIONS
    private lateinit var requestPermission: ActivityResultLauncher<Set<String>>

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        handleHealthConnectSDK()
        initPermissionLauncher(this) //펄미션 요청이 끝난 후 검증용 콜백

        CoroutineScope(Dispatchers.IO).launch {
            requestPermission(this@MainActivity)
        }
    }

    override fun onSuccess() {
        //mediatorLiveData로 동시에 LiveData 관리하려 했으나, 튜토리얼 -> 토큰여부 -> 페이저 순으로 체크의 우선순위가 있기 때문에 일단 if-else로 구성해야 할듯
        CoroutineScope(Dispatchers.Main).launch {
            // mainThread에서 관측
            checkTutorial()
        }

    }

    private fun checkTutorial() {
        viewModel.checkTutorialStatus().observe(this) { isComplete ->
            if (isComplete)
                //튜토리얼 완료했기 때문에 AuthToken 단계로 진입
                checkAuthToken()
            else
                //튜토리얼로 이동
                startActivityWithBackstackClear(TutorialActivity::class.java)
        }
    }

    private fun checkAuthToken() {
        viewModel.checkAuthToken().observe(this) { hasToken ->
            if (hasToken)
                //토큰이 있는경우 메인페이저로 이동
                startActivityWithBackstackClear(MainPagerActivity::class.java)
            else
                //로그인 화면으로 이동
                startActivityWithBackstackClear(AuthActivity::class.java) //login - 회원가입
        }
    }

    //sdk init
    private fun handleHealthConnectSDK() {
        val status: HealthConnectStatus = HealthConnectAPI.getSdkStatus(this)
        Log.i(LOG_HEADER, "Health Connect Status : $status")
        //이미 설치된 경우 핸들링 안함
        if (status == HealthConnectStatus.OK) return

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
    private fun initPermissionLauncher(callback : HealthConnectSuccessCallback) {
        //요청 contract와 callback
        requestPermission = registerForActivityResult(PermissionController.createRequestPermissionResultContract()) { granted ->
            //권한이 허용되지 않았을경우
            if (!granted.containsAll(permissions)) {
                Toast.makeText(this, R.string.health_permission_denied, Toast.LENGTH_SHORT).show()
                finish()
            }
            else
                callback.onSuccess()
        }
    }

    //HealthConnect SDK Permission request
    private suspend fun requestPermission(callback: HealthConnectSuccessCallback) {
        //요청될 permission
        val permissions = HealthConnectAPI.PERMISSIONS
        val healthConnectClient = HealthConnectAPI.getHealthConnectClient(applicationContext) //Singleton 준수하기 위한 Application Context 사용

        //이미 충분한 권한이 지급된경우 정상 작동하므로 callback 실행 후 return
        if (healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)) {
            callback.onSuccess()
            return
        }

        requestPermission.launch(permissions)
    }



}

// helath connect 설정이 모두 완료됐을때 수행하는 callback
interface HealthConnectSuccessCallback {
    fun onSuccess()
}

//뒤로가기 금지하고 액티비티 시작하는 확장함수
fun Activity.startActivityWithBackstackClear(targetClass : Class<*>) {
    startActivity(Intent(this, targetClass).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)))
}