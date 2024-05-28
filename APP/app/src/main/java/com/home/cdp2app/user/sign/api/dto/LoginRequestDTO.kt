package com.home.cdp2app.user.sign.api.dto

/**
 * 로그인 요청 DTO입니다.
 * @property email 로그인에 사용되는 이메일 값입니다.
 * @property password 로그인에 사용되는 비밀번호 값입니다.
 */
class LoginRequestDTO(val email : String, val password : String)