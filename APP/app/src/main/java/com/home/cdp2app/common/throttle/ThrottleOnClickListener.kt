package com.home.cdp2app.common.throttle

import android.view.View

/**
 * Button의 중복 클릭을 방지하기 위한 리스너 입니다.
 *
 * Button의 Enabled 속성등 '뷰'수준에서 중복으로 접근하지 못하게 하는 방식은 UI업데이트를 위한 틱을 기다려야 하므로 원천적으로 차단하기 어렵습니다.
 * 따라서, 따로 리스너를 만들어 클릭 시간을 비교하고, 이를 통해 클릭을 수행하도록 합니다.
 * @property throttleMillis 클릭을 제한할 밀리 세컨드입니다.
 * @property clickFunction 클릭시 수행할 함수입니다.
 * @property lastClickMillis 마지막으로 클릭이 허용되었던 밀리 세컨드값입니다. 현재 밀리세컨드와 해당 값의 차이가 throttleMillis보다 클경우 clickFunction이 수행되며, 기본값은 0입니다.
 */
class ThrottleOnClickListener(private val throttleMillis : Long, private val clickFunction : (View) -> Unit) : View.OnClickListener {

    private var lastClickMillis : Long = 0

    /**
     * throttleMillis를 받지 않는 생성자 입니다. throttleMillis가 300ms로 지정됩니다.
     */
    constructor(clickFunction: (View) -> Unit) : this(300L, clickFunction)

    override fun onClick(v: View) {
        //현재시간과 마지막 클릭시간의 차이가 쓰로틀보다 클경우
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickMillis >= throttleMillis) {
            lastClickMillis = currentTime //최종 클릭시간 업데이트
            clickFunction(v) //수행
        }
    }

}

/**
 * View의 확장함수로 제작하여 바로 적용할 수 있게한 함수입니다. ThrottleMillis를 지정할 수 있습니다.
 */
fun View.setThrottleClickListener(throttleMillis: Long, clickFunction: (View) -> Unit) {
    setOnClickListener(ThrottleOnClickListener(throttleMillis, clickFunction))
}

/**
 * View의 확장함수로 제작하여 바로 적용할 수 있게한 함수입니다. ThrottleMillis가 기본값인 300ms로 지정됩니다.
 */
fun View.setThrottleClickListener(clickFunction: (View) -> Unit) {
    setOnClickListener(ThrottleOnClickListener(clickFunction))
}