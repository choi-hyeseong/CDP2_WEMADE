package com.home.cdp2app.health.heart.repository


import com.home.cdp2app.health.heart.dao.HealthConnectHeartDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Instant

class HealthConnectHeartRepositoryTest {
    val heartDao = mockk<HealthConnectHeartDao>()

    @Test
    fun TEST_WRITE_RECORD() {
        //given
        val healthConnectRepository = HealthConnectHeartRepository(heartDao)
        coEvery { heartDao.writeHeartRate(any()) } returns mockk()

        //when
        runBlocking {
            healthConnectRepository.writeHeartRate(mockk())
        }
        //then
        coVerify(atLeast = 1) { heartDao.writeHeartRate(any()) }

    }

    @Test
    fun TEST_READ_RECORD() {
        val healthConnectRepository = HealthConnectHeartRepository(heartDao)
        coEvery { heartDao.readHeartRate(any(), any()) } returns listOf()

        runBlocking {
            healthConnectRepository.readHeartRate(Instant.now(), Instant.now())
        }
        coVerify(atLeast = 1) { heartDao.readHeartRate(any(), any()) }
    }
}
