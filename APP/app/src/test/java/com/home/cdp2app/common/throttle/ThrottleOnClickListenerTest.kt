package com.home.cdp2app.common.throttle

import android.view.View
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.powermock.reflect.Whitebox

class ThrottleOnClickListenerTest {

    @Test
    fun TEST_INIT_MILLIS_PROVIDE() {
        //밀리세컨드가 제공되는 경우
        val millis = 300L
        val function = {view : View -> view.isEnabled = true }
        val listener = ThrottleOnClickListener(millis, function) //신기한게 outer lambda로 하면 주소값이 달라지네

        // reflection 이용한 필드 검사
        val millisField = Whitebox.getField(ThrottleOnClickListener::class.java, "throttleMillis").get(listener) as Long
        val functionField = Whitebox.getField(ThrottleOnClickListener::class.java, "clickFunction").get(listener) as (View) -> Unit

        //값 비교
        assertEquals(millis, millisField)
        assertEquals(function, functionField)
    }

    @Test
    fun TEST_INIT_MILLIS_NOT_PROVIDE() {
        //밀리세컨드가 제공되지 않는 경우
        val function = {view : View -> view.isEnabled = true }
        val listener = ThrottleOnClickListener(function)

        // reflection 이용한 필드 검사
        val millisField = Whitebox.getField(ThrottleOnClickListener::class.java, "throttleMillis").get(listener) as Long
        val functionField = Whitebox.getField(ThrottleOnClickListener::class.java, "clickFunction").get(listener) as (View) -> Unit

        //값 비교
        assertEquals(300L, millisField)
        assertEquals(function, functionField)
    }

    @Test
    fun TEST_ONCLICK_IGNORE() {
        //throttle보다 빠르게 클릭해서 무시되는 경우
        var counter = 0
        // 300ms 리스너
        val listener = ThrottleOnClickListener {
            counter += 1 //closure 이용하여 카운터 값 증가
        }

        // 거의 1ms단위로 작동하므로 1회 빼고 동작하지 않음
        for (i in 1..10)
            listener.onClick(mockk())
        assertEquals(1, counter)
    }

    @Test
    fun TEST_ONCLICK_ACCEPT() {
        //throttle보다 느리게 클릭해서 작동되는 경우
        var counter = 0
        // 100ms 리스너
        val listener = ThrottleOnClickListener(100L) {
            counter += 1 //closure 이용하여 카운터 값 증가
        }

        // 105ms마다 동작하므로 적용됨
        for (i in 1..10) {
            listener.onClick(mockk())
            Thread.sleep(105)
        }
        assertEquals(10, counter)
    }

    @Test
    fun TEST_MILLIS_NOT_PROVIDE_EXTENSION() {
        //밀리세컨드가 제공되지 않은 확장함수 테스트
        val mockView : View = mockk()
        val captureListener : CapturingSlot<View.OnClickListener> = slot()
        var testCounter = 0
        every { mockView.setOnClickListener(capture(captureListener)) } returns mockk() //capture

        mockView.setThrottleClickListener { testCounter++ } //테스트용 카운터 값 증가

        verify(atLeast = 1) { mockView.setOnClickListener(any()) } //onclick 적용했는지
        val captured = captureListener.captured
        assertTrue(captured is ThrottleOnClickListener) // throttle인지

        // reflection 이용한 필드 검사
        val millisField = Whitebox.getField(ThrottleOnClickListener::class.java, "throttleMillis").get(captured) as Long
        assertEquals(300, millisField) //300ms로 throttle이 지정되었는지
        captured.onClick(mockk()) //수행했을때
        assertEquals(1, testCounter) //내부값이 수행됐는지
    }

    @Test
    fun TEST_MILLIS_PROVIDE_EXTENSION() {
        //밀리세컨드가 제공된 확장함수 테스트
        val mockView : View = mockk()
        val captureListener : CapturingSlot<View.OnClickListener> = slot()
        every { mockView.setOnClickListener(capture(captureListener)) } returns mockk() //capture

        mockView.setThrottleClickListener(750L) {}

        verify(atLeast = 1) { mockView.setOnClickListener(any()) } //onclick 적용했는지
        val captured = captureListener.captured
        assertTrue(captured is ThrottleOnClickListener) // throttle인지

        // reflection 이용한 필드 검사
        val millisField = Whitebox.getField(ThrottleOnClickListener::class.java, "throttleMillis").get(captured) as Long
        assertEquals(750, millisField) //750ms로 throttle이 지정되었는지
    }



}