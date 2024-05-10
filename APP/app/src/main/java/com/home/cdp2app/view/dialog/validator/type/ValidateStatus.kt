package com.home.cdp2app.view.dialog.validator.type

/**
 * 값저장시 validate한 결과를 나타내는 enum 입니다.
 * @property OK validate가 성공했습니다.
 * @property FIELD_EMPTY 값이 비어있습니다.
 * @property VALUE_ERROR - 올바른 값이 들어있지 않습니다. (숫자 필드에 문자열..)
 */
enum class ValidateStatus {
    OK, FIELD_EMPTY, VALUE_ERROR
}