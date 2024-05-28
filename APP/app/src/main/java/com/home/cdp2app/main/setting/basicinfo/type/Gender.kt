package com.home.cdp2app.main.setting.basicinfo.type

/**
 * 성별을 구성하는 enum
 * @property MAN 남성입니다
 * @property WOMAN 여성입니다
 */
enum class Gender {
    MAN, WOMAN;

    /**
     * Request DTO에 필요한 int 값으로 변환할때 사용하는 메소드
     * @return 남성일경우 1, 여성일경우 2 반환
     */
    fun toIntValue() : Int = if (this == MAN) 1 else 2
}