package com.home.cdp2app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.home.cdp2app.databinding.MainSettingBasicInfoBinding
import com.home.cdp2app.health.basic.repository.PreferenceBasicInfoRepository
import com.home.cdp2app.health.basic.type.Gender
import com.home.cdp2app.health.basic.usecase.LoadBasicInfo
import com.home.cdp2app.health.basic.usecase.SaveBasicInfo
import com.home.cdp2app.memory.SharedPreferencesStorage
import com.home.cdp2app.view.viewmodel.setting.BasicInfoViewModel

/**
 * 기본 건강정보 (BasicInfo)를 수정하고 관리하는 액티비티
 */
class BasicInfoActivity : AppCompatActivity() {

    // todo hilt inject

    private val repository by lazy {  PreferenceBasicInfoRepository(SharedPreferencesStorage(this)) }
    private val viewModel : BasicInfoViewModel by lazy {  BasicInfoViewModel(LoadBasicInfo(repository), SaveBasicInfo(repository)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = MainSettingBasicInfoBinding.inflate(layoutInflater)
        setContentView(bind.root)
        initListener(bind)
        initObserve(bind)
    }

    //키 부분 텍스트뷰 값 변경하는 메소드
    private fun setHeightText(bind: MainSettingBasicInfoBinding, value : Float) {
        bind.height.text = getString(R.string.height_format, value.toInt())
    }

    //몸무게 부분 텍스트뷰 값 변경하는 메소드
    private fun setWeightText(bind: MainSettingBasicInfoBinding, value : Float) {
        bind.weight.text = getString(R.string.weight_format, value.toInt())
    }

    private fun setAgeText(bind: MainSettingBasicInfoBinding, value: Float) {
        bind.age.text = getString(R.string.age_format, value.toInt())
    }

    //뷰 리스너 초기화 부분 (슬라이더, 저장버튼)
    private fun initListener(bind: MainSettingBasicInfoBinding) {
        //키 부분 변경 리스너
        bind.slider.addOnChangeListener { _, value, _ ->
           setHeightText(bind, value)
        }

        //몸무게 부분 변경 리스너
        bind.sliderWeight.addOnChangeListener { _, value, _ ->
            setWeightText(bind, value)
        }

        //나이부분 슬라이더 리스너
        bind.sliderAge.addOnChangeListener {_, value, _ ->
            setAgeText(bind, value)
        }

        //저장버튼 클릭 리스너
        bind.save.setOnClickListener {
            val height = bind.slider.value.toInt() //키 부분 슬라이더 파싱
            val weight = bind.sliderWeight.value.toInt() //몸무게 부분 슬라이더 파싱
            val age = bind.sliderAge.value.toInt() //나이부분 슬라이더 파싱
            val isSmoking = bind.smoker.isChecked //흡연자 여부 체크확인 (라디오 그룹으로 되어 있어 흡연자 아닐경우 비흡연자로 체크)
            val gender = if (bind.man.isChecked) Gender.MAN else Gender.WOMAN //성별도 동일하게 남성이 체크되어 있는경우 남자, 아닌경우 여성 체크
            viewModel.saveBasicInfo(height, weight, isSmoking, age, gender)
        }
    }

    //LiveData Observe
    private fun initObserve(bind: MainSettingBasicInfoBinding) {
        // 저장여부 observe
        viewModel.saveLiveData.observe(this) { event ->
            val isSaved = event.getContent() ?: return@observe //이벤트가 리스닝된경우 return, 아닌경우 content 가져옴
            if (isSaved) {
                Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show()
                finish() //이전 화면으로 돌아가기 위한 finish
            }
        }

        //건강정보 읽었을때 파싱
        viewModel.basicInfoLiveData.observe(this) { data ->
            val height = data.height.toFloat()
            bind.slider.value = height
            //슬라이더가 변경되지 않을경우 대비하여 수동으로 변경
            setHeightText(bind, height)

            val weight = data.weight.toFloat()
            bind.sliderWeight.value = weight
            setWeightText(bind, weight)

            //나이 부분
            val age = data.age.toFloat()
            bind.sliderAge.value = age
            setAgeText(bind, age)

            //흡연 여부
            if (data.isSmoking) bind.smoker.isChecked = true
            else bind.nonSmoker.isChecked = true

            //여성인경우
            if (data.gender == Gender.MAN) bind.man.isChecked = true
            else bind.woman.isChecked = true
        }
    }


}