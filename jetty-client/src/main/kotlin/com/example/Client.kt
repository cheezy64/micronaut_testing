package com.example

import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest
import org.eclipse.jetty.websocket.client.WebSocketClient
import java.net.URI
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@WebSocket
class MyWebSocket {
  var session: Session? = null
  private val closeLatch = CountDownLatch(1)
  @OnWebSocketConnect
  fun onConnect(session: Session) {
    println("Sending message: Hello server")
    this.session = session
    session.remote.sendString("Hello server")
  }

  @OnWebSocketMessage
  fun onMessage(message: String) {
    println("Message from Server: $message")
  }

  @OnWebSocketError
  fun onError(t: Throwable) {
    println("Error: " + t.message);
    closeLatch.countDown()
  }

  @OnWebSocketClose
  fun onClose(statusCode: Int, reason: String?) {
    println("WebSocket Closed. Code:$statusCode")
    session = null
    closeLatch.countDown()
  }

  fun hasErrorOrClosed(duration: Int, unit: TimeUnit): Boolean {
    return closeLatch.await(duration.toLong(), unit)
  }
}

fun main(args: Array<String>) {
  val client = WebSocketClient()
  val socket = MyWebSocket()
  client.start()

  val destUri = URI("ws://localhost:34242/test")
  val request = ClientUpgradeRequest()
  println("Connecting to: $destUri")

//  client.maxIdleTimeout = 5000
  client.asyncWriteTimeout = 1
  client.connect(socket, destUri, request)

    while (!socket.hasErrorOrClosed(10, TimeUnit.MILLISECONDS)) {
      socket.session?.remote?.sendString("ping")
      Thread.sleep(1000)
    }

  client.stop()
}

