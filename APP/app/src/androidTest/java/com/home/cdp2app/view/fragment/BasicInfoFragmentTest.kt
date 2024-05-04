package com.home.cdp2app.view.fragment

import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModel
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.slider.Slider
import com.home.ToastMatcher
import com.home.cdp2app.R
import com.home.cdp2app.databinding.FragmentTutorial1Binding
import com.home.cdp2app.health.basic.entity.BasicInfo
import com.home.cdp2app.health.basic.repository.BasicInfoRepository
import com.home.cdp2app.health.basic.type.Gender
import com.home.cdp2app.health.basic.usecase.LoadBasicInfo
import com.home.cdp2app.health.basic.usecase.SaveBasicInfo
import com.home.cdp2app.view.viewmodel.BasicInfoViewModel
import com.home.getOrAwaitValue
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

// basic info fragment runtime test
// 각 테스트는 독립적으로 수행됨
// robolectrictestrunner 이용해서 jvm 상에서 돌려도 되나, UI는 실제 안드로이드 런타임에서 동작해야 함으로 테스트 환경은 런타임으로
@RunWith(AndroidJUnit4::class)
class BasicInfoFragmentTest {

    val loadBasicInfo : LoadBasicInfo = mockk() //mock usecase
    val saveBasicInfo : SaveBasicInfo = mockk() //save usecase
    val viewModel : BasicInfoViewModel =  BasicInfoViewModel(loadBasicInfo, saveBasicInfo) //mock으로 된 값이 들어가므로 sharedPreference 신경 X
    val context : Context = InstrumentationRegistry.getInstrumentation().targetContext


    @Before
    fun beforeTest() {
        coEvery { loadBasicInfo(any()) } returns BasicInfo(150.0, 75.0, Gender.MAN, true)  //기본값 반환하게
        //fragment 실행, theme id 지정해줘야 오류 없음, factory로 vm 주입
        launchFragmentInContainer<BasicInfoFragment>( themeResId = R.style.Theme_CDP2app, factory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return BasicInfoFragment(viewModel)
            }
        })
    }

    //키 슬라이더 테스트
    @Test
    fun TEST_HEIGHT_SLIDER() {
        //슬라이더 값 변경시 슬라이더 변경되는지, 포맷 변경되는지
        onView(withId(R.id.slider)).perform(setSliderValue(170.0f))
        onView(withId(R.id.slider)).check(matches(withSliderValue(170.0f)))
        //R.string에 명시된 포맷에 맞게 값이 변경되는지, 이때는 170에 해당하는 값이 들어가야함
        onView(withId(R.id.height)).check(matches(withText(context.getString(R.string.height_format, 170))))
    }

    @Test
    fun TEST_WEIGHT_SLIDER() {
        onView(withId(R.id.slider_weight)).perform(setSliderValue(70.0f))
        onView(withId(R.id.slider_weight)).check(matches(withSliderValue(70.0f)))
        //R.string에 명시된 포맷에 맞게 값이 변경되는지, 이때는 70에 해당하는 값이 들어가야함
        onView(withId(R.id.weight)).check(matches(withText(context.getString(R.string.weight_format, 70))))
    }

    @Test
    fun TEST_SMOKING_CHECKBOX() {
        //체크박스 확인
        onView(withId(R.id.smoker)).perform(ViewActions.click()) //흡연자 체크
        onView(withId(R.id.smoker)).check(matches(isChecked())) //흡연자 체크되어 있는지
        onView(withId(R.id.non_smoker)).check(matches(isNotChecked())) //비흡연자 체크 안되어 있는지

        //비흡연자 체크박스 확인
        onView(withId(R.id.non_smoker)).perform(ViewActions.click()) //비흡연자 체크
        onView(withId(R.id.non_smoker)).check(matches(isChecked())) //비흡연자 체크되어 있는지
        onView(withId(R.id.smoker)).check(matches(isNotChecked())) //흡연자 체크 안되어 있는지
    }


    @Test
    fun TEST_GENDER_CHECKBOX() {
        //성별 체크박스 확인
        onView(withId(R.id.man)).perform(ViewActions.click()) //남성 체크
        onView(withId(R.id.man)).check(matches(isChecked())) //남성 체크되어 있는지
        onView(withId(R.id.woman)).check(matches(isNotChecked())) //여성 체크 안되어 있는지

        //비흡연자 체크박스 확인
        onView(withId(R.id.woman)).perform(ViewActions.click()) //여성 체크
        onView(withId(R.id.woman)).check(matches(isChecked())) //여성 체크되어 있는지
        onView(withId(R.id.man)).check(matches(isNotChecked())) //남성 체크 안되어 있는지
    }

    //livedata 값 로드 확인
    @Test
    fun TEST_LOAD_VALUE() {
        //livedata에서 값을 읽어오기 위해 지연을 발생, 여기서 coEvery 선언해도 의미 X, fragment 생성시 이미 observe 됨
        runBlocking(Dispatchers.Main) { viewModel.basicInfoLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) } //Thread sleep 대신 observe 관측시켜 완료될경우 수행되게 변경
        //맨 위에서 선언한 coEvery에서 반환하는 BasicInfo와 매칭되는지 확인
        onView(withId(R.id.slider)).check(matches(withSliderValue(150.0f))) //키 150 매칭
        onView(withId(R.id.slider_weight)).check(matches(withSliderValue(75.0f))) //몸무게 75 매칭
        onView(withId(R.id.smoker)).check(matches(isChecked())) //흡연자 체크
        onView(withId(R.id.man)).check(matches(isChecked())) //남성 체크
    }

    @Test
    fun TEST_SAVE_BUTTON() {
        val saveSlot : CapturingSlot<BasicInfo> = slot() //저장 확인용 슬롯
        coEvery { saveBasicInfo(capture(saveSlot)) } returns mockk() //저장 확인

        onView(withId(R.id.slider)).perform(setSliderValue(165.0f)) //키 슬라이더를 165로 클릭
        onView(withId(R.id.slider_weight)).perform(setSliderValue(75.0f)) //몸무게 슬라이더를 75.0으로 클릭
        onView(withId(R.id.non_smoker)).perform(ViewActions.click()) //비흡연자 클릭
        onView(withId(R.id.man)).perform(ViewActions.click()) //남성 클릭

        //저장버튼 클릭
        onView(withId(R.id.save)).perform(ViewActions.click())

        val capturedInfo = saveSlot.captured //저장 요청된 info
        assertEquals(165.0, capturedInfo.height, 0.0) //키 비교
        assertEquals(75.0, capturedInfo.weight, 0.0) //몸무게 비교
        assertEquals(Gender.MAN, capturedInfo.gender) //성별 비교
        assertEquals(false, capturedInfo.isSmoking) //비흡연자 비교

    }

    //저장시 토스트 테스트
    @Test
    fun TEST_SAVE_TOAST() {
        coEvery { saveBasicInfo(any()) } returns mockk() //저장 작동되게 every
        onView(withId(R.id.save)).perform(ViewActions.click()) //저장버튼 클릭
        runBlocking(Dispatchers.Main) { viewModel.saveLiveData.getOrAwaitValue(1, TimeUnit.SECONDS) } //Thread sleep 대신 observe 관측시켜 완료될경우 수행되게
        onView(withText(R.string.save_success)).inRoot(ToastMatcher().apply { matches(isDisplayed()) }) //토스트 발생 확인
    }

    fun withSliderValue(expectedValue: Float): Matcher<View?> {
        return object : BoundedMatcher<View?, Slider>(Slider::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("expected: $expectedValue")
            }

            override fun matchesSafely(slider: Slider?): Boolean {
                return slider?.value == expectedValue
            }
        }
    }
    fun setSliderValue(value: Float): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Slider value to $value"
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(Slider::class.java)
            }

            override fun perform(uiController: UiController?, view: View) {
                val seekBar = view as Slider
                seekBar.value = value
            }
        }
    }

}
