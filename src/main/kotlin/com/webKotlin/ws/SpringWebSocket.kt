package com.webKotlin.ws

import com.webKotlin.model.Manager
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.logging.Logger


@Component
class SpringWebSocket : TextWebSocketHandler() {
    private val log = Logger.getLogger(SpringWebSocket::class.java.name)

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val headers = session.handshakeHeaders
        val cookies = headers["cookie"]
        for (cookie in cookies!!) if (cookie.startsWith("JSESSIONID=")) {
            val httpSessionId = cookie.substring("JSESSIONID=".length)
            val user = Manager.get().findUserByHttpSessionId(httpSessionId)
            if (user != null) {
                user.session = session
            }
            break
        }
    }

    @Throws(Exception::class)
    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {


    }


    companion object {
        private const val IDMATCH = "idMatch"
    }
}