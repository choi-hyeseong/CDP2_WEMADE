package com.home.cdp2app.user.auth.sign.usecase

import com.home.cdp2app.rest.dto.RegisterResponse
import com.home.cdp2app.user.auth.sign.repository.UserRepository
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onError

/**
 * 회원가입을 수행하는 유스케이스입니다.
 * @property userRepository 유저 API에 접근하는 레포지토리 입니다.
 */
class RegisterUseCase(private val userRepository: UserRepository) {

    /**
     * 실제 회원가입을 수행하는 메소드입니다.
     * @param email 회원가입할 이메일 입니다.
     * @param password 회원가입할 비밀번호 입니다.
     * @param nickname 회원가입할 별명입니다.
     * @return 회원가입의 결과를 ApiResponse에 담아 전달합니다.
     * @see UserRepository.register
     */
    suspend operator fun invoke(email : String, password : String, nickname : String) : ApiResponse<RegisterResponse> {
        return userRepository.register(email, password, nickname)
    }
}