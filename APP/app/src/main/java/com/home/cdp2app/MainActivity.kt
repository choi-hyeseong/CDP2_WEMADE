package com.home.cdp2app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.home.cdp2app.databinding.ActivityMainBinding
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //For Web test
        CoroutineScope(Dispatchers.IO).launch {
            val response = WebComponent.getTestService().getIPAddress()
            val result = response.getOrNull()
            withContext(Dispatchers.Main) {
                if (result == null)
                    Toast.makeText(this@MainActivity, "요청에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                else
                    bind.mainText.text = "Hello World! IP : ${result.ip}"
            }
        }
    }
}