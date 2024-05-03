package com.home.cdp2app.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.home.cdp2app.R
import com.home.cdp2app.databinding.BasicInfoBinding
import com.home.cdp2app.health.basic.type.Gender
import com.home.cdp2app.view.viewmodel.BasicInfoViewModel

/**
 * 기본 건강정보 (BasicInfo)를 수정하고 관리하는 프래그먼트
 */
class BasicInfoFragment(private val viewModel: BasicInfoViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val bind = BasicInfoBinding.inflate(inflater)
        initListener(bind)
        initObserve(bind)
        return bind.root
    }

    //키 부분 텍스트뷰 값 변경하는 메소드
    private fun setHeightText(bind: BasicInfoBinding, value : Float) {
        bind.height.text = getString(R.string.height_format, value.toInt())
    }

    //몸무게 부분 텍스트뷰 값 변경하는 메소드
    private fun setWeightText(bind: BasicInfoBinding, value : Float) {
        bind.weight.text = getString(R.string.weight_format, value.toInt())
    }

    //뷰 리스너 초기화 부분 (슬라이더, 저장버튼)
    private fun initListener(bind: BasicInfoBinding) {
        //키 부분 변경 리스너
        bind.slider.addOnChangeListener { _, value, _ ->
           setHeightText(bind, value)
        }

        //몸무게 부분 변경 리스너
        bind.sliderWeight.addOnChangeListener { _, value, _ ->
            setWeightText(bind, value)
        }

        //저장버튼 클릭 리스너
        bind.save.setOnClickListener {
            val height = bind.slider.value.toInt() //키 부분 슬라이더 파싱
            val weight = bind.sliderWeight.value.toInt() //몸무게 부분 슬라이더 파싱
            val isSmoking = bind.smoker.isChecked //흡연자 여부 체크확인 (라디오 그룹으로 되어 있어 흡연자 아닐경우 비흡연자로 체크)
            val gender = if (bind.man.isChecked) Gender.MAN else Gender.WOMAN //성별도 동일하게 남성이 체크되어 있는경우 남자, 아닌경우 여성 체크
            viewModel.saveBasicInfo(height, weight, isSmoking, gender)
        }
    }

    //LiveData Observe
    private fun initObserve(bind: BasicInfoBinding) {
        // 저장여부 observe
        viewModel.saveLiveData.observe(viewLifecycleOwner) { event ->
            val isSaved = event.getContent() ?: return@observe //이벤트가 리스닝된경우 return, 아닌경우 content 가져옴
            if (isSaved)
                Toast.makeText(requireContext(), R.string.save_success, Toast.LENGTH_SHORT).show()
        }

        //건강정보 읽었을때 파싱
        viewModel.basicInfoLiveData.observe(viewLifecycleOwner) { data ->
            val height = data.height.toFloat()
            bind.slider.value = height
            //슬라이더가 변경되지 않을경우 대비하여 수동으로 변경
            setHeightText(bind, height)
            val weight = data.weight.toFloat()
            bind.sliderWeight.value = weight
            setWeightText(bind, weight)

            //흡연 여부
            if (data.isSmoking) bind.smoker.isChecked = true
            else bind.nonSmoker.isChecked = true

            //여성인경우
            if (data.gender == Gender.MAN) bind.man.isChecked = true
            else bind.woman.isChecked = true
        }
    }


}