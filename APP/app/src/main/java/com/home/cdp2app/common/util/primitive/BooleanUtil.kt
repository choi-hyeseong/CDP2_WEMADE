package com.home.cdp2app.common.util.primitive

/**
 * Boolean을 int형으로 변경하는 확장 함수
 * @return true일경우 1 아닐경우 0을 반환합니다. (C Like)
 */
fun Boolean.toInt() : Int = if (this) 1 else 0