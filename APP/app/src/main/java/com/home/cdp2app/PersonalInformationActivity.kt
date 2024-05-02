package com.home.cdp2app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.home.cdp2app.databinding.ActivityPersonalInformationBinding

class PersonalInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}