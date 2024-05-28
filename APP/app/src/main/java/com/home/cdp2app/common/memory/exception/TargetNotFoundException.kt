package com.home.cdp2app.common.memory.exception

/**
 * LocalDataStorage에서 해당 객체를 저장소에서 찾지 못했을경우 발생합니다. (키가 없거나 등등..)
 */
class TargetNotFoundException(message : String) : Exception(message)