package com.example

import io.micronaut.http.HttpRequest
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import jakarta.inject.Singleton

@Singleton
@ServerWebSocket("/test")
class TestWebsocketServer {
    @OnOpen
    fun onOpen(session: WebSocketSession, request: HttpRequest<*>) {
        println("++++++ Opening session: session=$session $request")
    }

    @OnClose
    fun onClose(session: WebSocketSession) {
        println("------ Session closed: session=$session")
    }

    @OnMessage(maxPayloadLength = 50 * 1024 * 1024)
    fun onMessage(session: WebSocketSession, message: ByteArray) {
        println("...... Message received: session=$session ${String(message)}")
    }
}