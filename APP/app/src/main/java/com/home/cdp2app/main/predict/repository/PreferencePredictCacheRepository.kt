package com.home.cdp2app.main.predict.repository

import com.home.cdp2app.common.memory.LocalDataStorage
import com.home.cdp2app.main.predict.entity.PredictResult

class PreferencePredictCacheRepository(private val storage: LocalDataStorage) : PredictCacheRepository {

    private val KEY = "PREDICT_KEY"

    //run catching 이용해서 Exception 발생시 null 리턴
    override suspend fun getPredictResult(): PredictResult? {
        return kotlin.runCatching {
            storage.loadObject(KEY, PredictResult::class)
        }.getOrNull()
    }

    override suspend fun savePredictResult(predictResult: PredictResult): Boolean {
        return storage.saveObject(KEY, predictResult)
    }


}