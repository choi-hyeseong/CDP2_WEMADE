package com.home.cdp2app.rest.dto

/**
 * 회원가입 요청 DTO입니다.
 * @property email 회원가입에 사용되는 이메일 값입니다.
 * @property password 회원가입에 사용되는 비밀번호 값입니다.
 * @property nickname 회원가입에 사용되는 닉네임 값입니다.
 */
data class RegisterRequestDTO(val email : String, val password : String, val nickname : String)