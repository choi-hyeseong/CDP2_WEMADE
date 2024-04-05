package com.home.cdp2app.health.healthconnect.repository

import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.home.cdp2app.health.healthconnect.dao.HealthConnectDao
import io.mockk.MockK
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

// dao test가 아니기 때문에 repository - dao 요청 전달 여부만 확인하기
class HealthConnectRepositoryTest {

    val healthConnectDao = mockk<HealthConnectDao>()

    @Test
    fun TEST_WRITE_RECORD() {
        //given
        val healthConnectRepository = HealthConnectRepository(healthConnectDao)
        coEvery { healthConnectDao.insertRecord(any()) } returns mockk()

        //when
        runBlocking {
            healthConnectRepository.insertRecord(mockk())
        }
        //then
        coVerify(atLeast = 1) { healthConnectDao.insertRecord(any()) }

    }

    @Test
    fun TEST_READ_RECORD() {
        val healthConnectRepository = HealthConnectRepository(healthConnectDao)
        val request = ReadRecordsRequest(HeartRateRecord::class, TimeRangeFilter.Companion.before(
            Instant.now()))
        coEvery { healthConnectDao.readRecord(request) } returns listOf()

        runBlocking {
            healthConnectRepository.readRecord(request)
        }
        coVerify(atLeast = 1) { healthConnectDao.readRecord(request) }
    }
}