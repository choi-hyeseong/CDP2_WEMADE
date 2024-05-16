package com.home.cdp2app.valid.type

/**
 * 값저장시 validate한 결과를 나타내는 enum 입니다.
 *
 * 추후 개선이 가능하다면 ValidateResult(status : ValidateStatus, field : String)의 형태로 개선, 어떤 필드에서 검증이 통과하지 못했는지 확인할 수 있도록..
 * @property OK validate가 성공했습니다.
 * @property FIELD_EMPTY 값이 비어있습니다.
 * @property VALUE_ERROR 올바른 값이 들어있지 않습니다. (숫자 필드에 문자열..)
 * @property DUPLICATE_REQUIRE 중복된 값이 들어가야 합니다. (비밀번호 재입력등)
 */
enum class ValidateStatus {
    OK, FIELD_EMPTY, VALUE_ERROR
}