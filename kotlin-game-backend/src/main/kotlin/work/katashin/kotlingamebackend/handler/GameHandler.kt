package work.katashin.kotlingamebackend.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import work.katashin.kotlingamebackend.manager.RoomManager

class GameHandler : TextWebSocketHandler() {

    override fun afterConnectionClosed(session: WebSocketSession?, status: CloseStatus?) {
        session?.let {
            RoomManager.removeSession(session)
        }
    }

    override fun handleTextMessage(session: WebSocketSession?, message: TextMessage?) {
        session?.let {
            RoomManager.dealWithGameData(session, jacksonObjectMapper().readValue(message?.payload?:"{}"))
        }
    }
}
