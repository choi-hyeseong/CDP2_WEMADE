package com.home.cdp2app.user.sign.usecase

import com.home.cdp2app.rest.dto.LoginResponseDTO
import com.home.cdp2app.user.sign.repository.UserRepository
import com.skydoves.sandwich.ApiResponse

/**
 * 로그인을 수행하는 유스케이스
 * @property userRepository api 요청을 수행하는 레포지토리입니다.
 */
class LoginUseCase(private val userRepository: UserRepository) {

    /**
     * 실제 요청이 이루어지는 메소드 입니다.
     * @param email 로그인할 이메일 입니다.
     * @param password 로그인할 비밀번호 입니다.
     * @return 로그인 결과를 ApiResponse에 담아 리턴합니다
     * @see UserRepository.login
     */
    suspend operator fun invoke(email : String, password : String) : ApiResponse<LoginResponseDTO> {
        return userRepository.login(email, password)
    }
}