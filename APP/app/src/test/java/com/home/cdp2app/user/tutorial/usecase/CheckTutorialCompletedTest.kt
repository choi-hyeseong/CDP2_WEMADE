package com.home.cdp2app.user.tutorial.usecase

import com.home.cdp2app.user.tutorial.repository.TutorialRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class CheckTutorialCompletedTest {

    private val repository : TutorialRepository = mockk() //mock repo
    private val checkTutorialCompleted = CheckTutorialCompleted(repository)

    @Test
    fun TEST_TUTORIAL_COMPLETED() {
        coEvery { repository.checkCompleted() } returns false //튜토리얼 미 완료 return

        runBlocking {
            val result = checkTutorialCompleted()
            coVerify(atLeast = 1) { repository.checkCompleted() }
            assertFalse(result)
        }
    }
}