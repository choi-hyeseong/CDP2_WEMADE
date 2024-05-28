package com.home.cdp2app.user.tutorial.usecase

import com.home.cdp2app.tutorial.repository.TutorialRepository
import com.home.cdp2app.tutorial.usecase.SaveTutorialCompleted
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class SaveTutorialCompletedTest {

    private val repository : TutorialRepository = mockk() //mock repo
    private val saveTutorialCompleted = SaveTutorialCompleted(repository)

    @Test
    fun TEST_TUTORIAL_COMPLETED() {
        coEvery { repository.setCompleted() } returns mockk() //작동되게 mock

        runBlocking {
            saveTutorialCompleted()
            coVerify(atLeast = 1) { repository.setCompleted() }
        }
    }
}