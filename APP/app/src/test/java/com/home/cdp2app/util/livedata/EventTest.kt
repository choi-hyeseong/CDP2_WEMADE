package com.home.cdp2app.util.livedata

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class EventTest {

    @Test
    fun TEST_GET_CONTENT_SUCCESSFUL() {
        val content : String = "Hello World!" //내용물
        val event = Event(content) //최초로 생성된 event
        val unWrappedContent = event.getContent() //getContent를 수행함. 아직 get을 수행한적 없으므로 content를 반환
        assertNotNull(unWrappedContent)
        assertEquals(content, unWrappedContent)
    }

    @Test
    fun TEST_GET_CONTENT_FAIL() {
        val content : String = "Hello World!" //내용물
        val event = Event(content) //최초로 생성된 event
        event.getContent() //1회의 consume이 발생해 isHandled 변수 true로 지정됨. 이제는 null 반환됨
        val unWrappedContent = event.getContent()
        assertNull(unWrappedContent)
    }

    @Test
    fun TEST_GET_CONTENT_FORCE() {
        val content : String = "Hello World!" //내용물
        val event = Event(content) //최초로 생성된 event
        event.getContent() //1회의 consume이 발생해 isHandled 변수 true로 지정됨. 이제는 null 반환됨
        val unWrappedContent = event.getContentForce() //force로 가져옴
        assertNotNull(unWrappedContent)
        assertEquals(content, unWrappedContent)
    }

    @Test
    fun TEST_IS_HANDLED() {
        val content : String = "HellO!"
        val event = Event(content)
        event.getContent()
        assertTrue(event.isHandled)
    }

    @Test
    fun TEST_IS_NOT_HANDLED() {
        val content : String = "HellO!"
        val event = Event(content)
        assertFalse(event.isHandled)
    }
}